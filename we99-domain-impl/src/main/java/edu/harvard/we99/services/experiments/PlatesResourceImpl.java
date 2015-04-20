package edu.harvard.we99.services.experiments;

import edu.harvard.we99.domain.Compound;
import edu.harvard.we99.domain.Experiment;
import edu.harvard.we99.domain.Plate;
import edu.harvard.we99.domain.PlateMap;
import edu.harvard.we99.domain.PlateMapMergeInfo;
import edu.harvard.we99.domain.Well;
import edu.harvard.we99.domain.WellLabelMapping;
import edu.harvard.we99.domain.WellMap;
import edu.harvard.we99.domain.WellType;
import edu.harvard.we99.domain.lists.Plates;
import edu.harvard.we99.services.io.PlateCSVReader;
import edu.harvard.we99.services.io.PlateWithOptionalResults;
import edu.harvard.we99.services.storage.CompoundStorage;
import edu.harvard.we99.services.storage.PlateMapStorage;
import edu.harvard.we99.services.storage.PlateStorage;
import edu.harvard.we99.services.storage.PlateTypeStorage;
import edu.harvard.we99.services.storage.ResultStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Generated;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * @author mford
 */
public abstract class PlatesResourceImpl implements PlatesResource {

    private static final Logger log = LoggerFactory.getLogger(PlatesResourceImpl.class);

    private final PlateStorage plateStorage;
    private final CompoundStorage compoundStorage;
    private final PlateTypeStorage plateTypeStorage;
    private final PlateMapStorage plateMapStorage;
    private final ResultStorage resultStorage;
    private Experiment experiment;

    public PlatesResourceImpl(PlateStorage plateStorage, CompoundStorage compoundStorage,
                              PlateTypeStorage plateTypeStorage, PlateMapStorage plateMapStorage,
                              ResultStorage resultStorage) {
        this.plateStorage = plateStorage;
        this.compoundStorage = compoundStorage;
        this.plateTypeStorage = plateTypeStorage;
        this.plateMapStorage = plateMapStorage;
        this.resultStorage = resultStorage;
    }

    @Override
    public Plate create(Plate plate) {
        plate.setId(null);
        plate.setExperimentId(experiment.getId());
        return plateStorage.create(plate);
    }

    @Override
    public Plate create(PlateMapMergeInfo mergeInfo) {
        validate(mergeInfo);
        try {
            PlateMap plateMap = plateMapStorage.get(mergeInfo.getPlateMapId());

            if (mergeInfo.getPlateName() == null) {
                String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
                mergeInfo.setPlateName("My Plate " + timeStamp);
            }

            Plate plate = new Plate()
                    .setName(mergeInfo.getPlateName())
                    .setExperimentId(experiment.getId())
                    .setPlateType(mergeInfo.getPlateType());

            // copy all of the wells from the plate map to the plate
            // wells are copied by their coordinate
            // each of the wells

            for (WellMap wm : plateMap.getWells().values()) {
                Well well = new Well().setType(wm.getType());
                well.setCoordinate(wm.getCoordinate());
                well.setLabels(wm.getLabels());
                plate.getWells().put(wm.getCoordinate(), well);
                if (well.getType() != WellType.EMPTY) {
                    well.dose(mergeInfo.getMappings().get(well.getContentsLabel()).take());
                }
            }

            // get all of the compounds
            Set<Compound> compounds = new HashSet<>();
            plate.getWells().values().stream().forEach(w -> w.getContents().forEach(d -> compounds.add(d.getCompound())));
            // figure out what their id's are
            Map<Compound, Long> resolvedCompounds = compoundStorage.resolveIds(compounds);
            // assign those id's
            plate.getWells().values().stream().forEach(w -> w.getContents().forEach(d -> d.getCompound().setId(resolvedCompounds.get(d.getCompound()))));

            // store the plate
            return plateStorage.create(plate);
        } catch(Exception e) {
            log.error("Error creating a plate from merge info", e);
            throw new WebApplicationException(Response.status(500).build());
        }
    }

    private void validate(PlateMapMergeInfo mergeInfo) {
        try {
            for (WellLabelMapping wlm : mergeInfo.getMappings().values()) {
                if (wlm.getWellType() == WellType.EMPTY) {
                    if (wlm.getDose() != null) {
                        throw new WebApplicationException(
                                Response.status(409)
                                        .entity("Wells marked as empty cannot contain a compound")
                                        .build());
                    }
                } else {
                    if (wlm.getDose() == null) {
                        throw new WebApplicationException(
                                Response.status(409)
                                        .entity("Non-empty wells must have a Dose")
                                        .build());
                    }
                    if (wlm.getDose().getCompound() == null) {
                        String message = "Dose in well label %s is missing its compound";
                        throw new WebApplicationException(
                                Response.status(409)
                                        .entity(String.format(message, wlm.getLabel()))
                                        .build());
                    }
                    if (wlm.getDose().getAmount() == null) {
                        String message = "Dose in well label %s is missing its amount";
                        throw new WebApplicationException(
                                Response.status(409)
                                        .entity(String.format(message, wlm.getLabel()))
                                        .build());
                    }
                }
            }
        } catch(WebApplicationException e) {
            log.error("error validating plate merge request:" + e.getResponse().getEntity().toString());
            throw e;
        }
    }

    @Override
    public Plate create(String name, String plateTypeName, InputStream csv) {
        PlateCSVReader reader = new PlateCSVReader();
        try (Reader r = new BufferedReader(new InputStreamReader(csv))) {
            if (name == null) {
                name = UUID.randomUUID().toString();
            }
            PlateWithOptionalResults read = reader.read(r);
            Plate plate = read.getPlate().setName(name);
            plate.setExperimentId(experiment.getId());
            plate.setPlateType(plateTypeStorage.getByName(plateTypeName));
            // walk all of the compounds to set their id's or leave null if we're persisting
            Set<Compound> compounds = new HashSet<>();
            plate.getWells().values().forEach(well -> well.getContents().forEach(d -> compounds.add(d.getCompound())));
            Map<Compound, Long> resolvedIds = compoundStorage.resolveIds(compounds);
            plate.getWells().values().forEach(well -> well.getContents().forEach(d -> d.getCompound().setId(resolvedIds.get(d.getCompound()))));
            Plate created = plateStorage.create(plate);

            if (read.getResults() != null) {
                read.getResults().setPlate(created);
                resultStorage.create(read.getResults());
                created.setHasResults(true);
            }

            return created;
        } catch (IOException e) {
            log.error("error parsing csv", e);
            throw new WebApplicationException(Response.status(409).build());
        } catch (Exception e) {
            log.error("error inserting plate", e);
            throw new WebApplicationException(Response.status(500).build());
        }
    }

    @Override
    public Plates list(Integer page, Integer pageSize, String typeAhead) {
        return plateStorage.listAll(experiment.getId(), page, pageSize, typeAhead);
    }

    @Override
    public PlateResource getPlates(Long plateId) {
        PlateResource pr = createPlateResource();
        pr.setExperiment(experiment);
        pr.setPlateId(plateId);
        return pr;
    }

    protected abstract PlateResource createPlateResource();

    @Override
    @Generated(value = "generated by IDE")
    public void setExperiment(Experiment experiment) {
        this.experiment = experiment;
    }

    @Override
    @Generated(value = "generated by IDE")
    public Experiment getExperiment() {
        return experiment;
    }
}
