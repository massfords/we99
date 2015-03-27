package edu.harvard.we99.services.experiments;

import edu.harvard.we99.domain.Compound;
import edu.harvard.we99.domain.Experiment;
import edu.harvard.we99.domain.Plate;
import edu.harvard.we99.domain.PlateMap;
import edu.harvard.we99.domain.PlateMapMergeInfo;
import edu.harvard.we99.domain.Well;
import edu.harvard.we99.domain.WellMap;
import edu.harvard.we99.domain.WellType;
import edu.harvard.we99.domain.lists.Plates;
import edu.harvard.we99.services.io.PlateCSVReader;
import edu.harvard.we99.services.storage.CompoundStorage;
import edu.harvard.we99.services.storage.PlateMapStorage;
import edu.harvard.we99.services.storage.PlateStorage;
import edu.harvard.we99.services.storage.PlateTypeStorage;
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
    private Experiment experiment;

    public PlatesResourceImpl(PlateStorage plateStorage, CompoundStorage compoundStorage,
                              PlateTypeStorage plateTypeStorage, PlateMapStorage plateMapStorage) {
        this.plateStorage = plateStorage;
        this.compoundStorage = compoundStorage;
        this.plateTypeStorage = plateTypeStorage;
        this.plateMapStorage = plateMapStorage;
    }

    @Override
    public Plate create(Plate plate) {
        plate.setId(null);
        plate.setExperiment(experiment);
        return plateStorage.create(plate);
    }

    @Override
    public Plate create(PlateMapMergeInfo mergeInfo) {
        try {
            PlateMap plateMap = plateMapStorage.get(mergeInfo.getPlateMapId());

            Plate plate = new Plate()
                    .withName(mergeInfo.getPlateName())
                    .withExperiment(experiment)
                    .withPlateType(mergeInfo.getPlateType());

            // copy all of the wells from the plate map to the plate
            // wells are copied by their coordinate
            // each of the wells

            for (WellMap wm : plateMap.getWells().values()) {
                Well well = new Well().withType(wm.getType());
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

    @Override
    public Plate create(String name, String plateTypeName, InputStream csv) {
        PlateCSVReader reader = new PlateCSVReader();
        try (Reader r = new BufferedReader(new InputStreamReader(csv))) {
            if (name == null) {
                name = UUID.randomUUID().toString();
            }
            Plate plate = reader.read(r).withName(name);
            plate.setExperiment(experiment);
            plate.setPlateType(plateTypeStorage.getByName(plateTypeName));
            // walk all of the compounds to set their id's or leave null if we're persisting
            Set<Compound> compounds = new HashSet<>();
            plate.getWells().values().forEach(well -> well.getContents().forEach(d -> compounds.add(d.getCompound())));
            Map<Compound, Long> resolvedIds = compoundStorage.resolveIds(compounds);
            plate.getWells().values().forEach(well -> well.getContents().forEach(d -> d.getCompound().setId(resolvedIds.get(d.getCompound()))));
            Plate created = plateStorage.create(plate);
            return created;
        } catch (IOException e) {
            log.error("error parsing csv", e);
            throw new WebApplicationException(Response.status(409).build());
        }
    }

    @Override
    public Plates list(Integer page) {
        return plateStorage.listAll(experiment.getId(), page);
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
