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
import edu.harvard.we99.domain.results.PlateResult;
import edu.harvard.we99.services.io.MultiResultCollector;
import edu.harvard.we99.services.io.PlateCSVReader;
import edu.harvard.we99.services.io.PlateResultCollector;
import edu.harvard.we99.services.io.PlateResultsReader;
import edu.harvard.we99.services.io.PlateWithOptionalResults;
import edu.harvard.we99.services.storage.CompoundStorage;
import edu.harvard.we99.services.storage.PlateMapStorage;
import edu.harvard.we99.services.storage.PlateStorage;
import edu.harvard.we99.services.storage.PlateTypeStorage;
import edu.harvard.we99.services.storage.ResultStorage;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
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
import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;

import static edu.harvard.we99.services.experiments.PlateResultResourceImpl.compute;
import static edu.harvard.we99.services.experiments.PlateResultResourceImpl.createReader;

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
        validate(mergeInfo, new SinglePlateMergeValidation());
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

            wellLabelMappingToWell(mergeInfo, plateMap, plate);

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
    public Plates bulkCreate(PlateMapMergeInfo mergeInfo, InputStream csv) {
        assert mergeInfo != null;
        try {
            validate(mergeInfo, new CompoundlessPlateValidation());
            Set<Compound> compoundSet = parse(csv);
            if (compoundSet.isEmpty()) {
                throw new WebApplicationException(Response.status(409).build());
            }
            Map<Compound, Long> resolveIds = compoundStorage.resolveIds(compoundSet);

            PlateMap plateMap = plateMapStorage.get(mergeInfo.getPlateMapId());

            String namePattern = "My Plate " + DateTime.now() + " ";

            // walk the compoundSet and create one plate for each of the given compounds
            List<Plate> list = new ArrayList<>();
            List<Compound> compounds = new ArrayList<>(resolveIds.keySet());

            Plate plate;

            do {
                plate = new Plate()
                        .setName(namePattern + list.size())
                        .setExperimentId(experiment.getId())
                        .setPlateType(mergeInfo.getPlateType());
                list.add(plate);

                // ensure that our well mappings are in the default position
                mergeInfo.getMappings().values().forEach(
                        wlm -> wlm.setDoses(null)
                );

                // assign compounds to each well mapping for the  plate
                // pop a compound off of the list and assign it to the first
                // well label mapping. There should be at least one available.
                // If we've run out within this for loop then we'll just mark
                // the remaining mappings as empty
                mergeInfo.getMappings().values()
                        .stream()
                        .filter(wlm -> wlm.getWellType() != WellType.EMPTY)
                        .forEach(wlm -> {
                            if (!compounds.isEmpty()) {
                                wlm.getDose().setCompound(compounds.remove(0));
                            } else {
                                wlm.setDose(null);
                                wlm.setWellType(WellType.EMPTY);
                            }
                        });

                wellLabelMappingToWell(mergeInfo, plateMap, plate);

            } while (!compounds.isEmpty());

            return plateStorage.create(list);
        } catch(WebApplicationException e) {
            log.error("Error with bulk plate add", e);
            throw e;
        } catch(Exception e) {
            log.error("Error with bulk plate add", e);
            throw new WebApplicationException(Response.serverError().build());
        }
    }

    @Override
    public Response bulkResults(String format, InputStream csv) {
        assert csv != null;

        String source;
        try {

            List<Plate> plates = plateStorage.getAllWithWells(experiment.getId()).getValues();

            source = IOUtils.toString(csv);

            PlateResultsReader reader = createReader(format);
            PlateResultCollector collector = new MultiResultCollector();
            reader.read(new BufferedReader(new StringReader(source)), collector);
            List<PlateResult> results = collector.getResults();
            if (results.size() != plates.size()) {
                String errorMessage = "wrong number of results for experiment. Results Passed: %d Actual Plates: %d";
                throw new WebApplicationException(String.format(errorMessage, results.size(), plates.size()),
                        Response.status(409)
                                .entity(errorMessage)
                                .build());
            }
            for (PlateResult pr : results) {
                pr.setPlate(plates.remove(0));
                pr.setMetrics(compute(pr, pr.getPlate()));
            }
            resultStorage.create(results);
            return Response.ok().build();
        } catch(WebApplicationException e) {
            log.error(e.getMessage(), e);
            throw e;
        } catch (IOException e) {
            log.error("error parsing results csv", e);
            throw new WebApplicationException(Response.serverError().build());
        } catch (Exception e) {
            log.error("error inserting results csv", e);
            throw new WebApplicationException(Response.serverError().build());
        }
    }

    private void validate(PlateMapMergeInfo mergeInfo, Function<WellLabelMapping, Void> validationFunction) {
        try {
            mergeInfo.getMappings().values().forEach(validationFunction::apply);
        } catch(WebApplicationException e) {
            log.error("error validating plate merge request:" + e.getResponse().getEntity().toString());
            throw e;
        }
    }

    private static class SinglePlateMergeValidation extends CompoundlessPlateValidation {

        @Override
        public Void apply(WellLabelMapping wlm) {
            if (wlm.getWellType() != WellType.EMPTY && wlm.getDose().getCompound() == null) {
                String message = "Dose in well label %s is missing its compound";
                throw new WebApplicationException(
                        Response.status(409)
                                .entity(String.format(message, wlm.getLabel()))
                                .build());
            }
            return null;
        }
    }

    private static class CompoundlessPlateValidation implements Function<WellLabelMapping, Void> {

        @Override
        public Void apply(WellLabelMapping wlm) {
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
                if (wlm.getDose().getAmount() == null) {
                    String message = "Dose in well label %s is missing its amount";
                    throw new WebApplicationException(
                            Response.status(409)
                                    .entity(String.format(message, wlm.getLabel()))
                                    .build());
                }
            }
            return null;
        }
    }

    @Override
    public Plates create(String name, String plateTypeName, InputStream csv) {
        Plates plates = new Plates();
        PlateCSVReader reader = new PlateCSVReader();
        try (Reader r = new BufferedReader(new InputStreamReader(csv))) {
            if (name == null) {
                name = UUID.randomUUID().toString();
            }
            List<PlateWithOptionalResults> read = reader.read(r);
            for(PlateWithOptionalResults por : read) {
                Plate plate = por.getPlate().setName(name);
                plate.setExperimentId(experiment.getId());
                plate.setPlateType(plateTypeStorage.getByName(plateTypeName));
                // walk all of the compounds to set their id's or leave null if we're persisting
                Set<Compound> compounds = new HashSet<>();
                plate.getWells().values().forEach(well -> well.getContents().forEach(d -> compounds.add(d.getCompound())));
                Map<Compound, Long> resolvedIds = compoundStorage.resolveIds(compounds);
                plate.getWells().values().forEach(well -> well.getContents().forEach(d -> d.getCompound().setId(resolvedIds.get(d.getCompound()))));
                Plate created = plateStorage.create(plate);

                if (por.getResults() != null) {
                    por.getResults().setPlate(created);
                    resultStorage.create(por.getResults());
                    created.setHasResults(true);
                }

                plates.getValues().add(plate);
            }
            plates.setPage(0)
                    .setPageSize(plates.getValues().size())
                    .setTotalCount((long) plates.getValues().size());
            return plates;
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

    private Set<Compound> parse(InputStream csv) {
        Set<Compound> compoundSet = new LinkedHashSet<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(csv))) {
            String line;
            while((line = br.readLine()) != null) {
                String name = line.trim();
                if (!name.startsWith("#") && StringUtils.isNotBlank(name)) {
                    compoundSet.add(new Compound(name));
                }
            }
        } catch (IOException e) {
            throw new WebApplicationException(Response.serverError().build());
        }
        return compoundSet;
    }

    private void wellLabelMappingToWell(PlateMapMergeInfo mergeInfo, PlateMap plateMap, Plate plate) {
        for (WellMap wm : plateMap.getWells().values()) {
            Well well = new Well().setType(wm.getType());
            well.setCoordinate(wm.getCoordinate());
            well.setLabels(wm.getLabels());
            plate.getWells().put(wm.getCoordinate(), well);
            if (well.getType() != WellType.EMPTY) {
                WellLabelMapping wlm = mergeInfo.getMappings().get(well.getContentsLabel());
                // make sure our well hasn't been flipped to empty
                if (wlm.getWellType() == WellType.EMPTY) {
                    well.setType(WellType.EMPTY);
                } else {
                    well.dose(wlm.take());
                }
            }
        }
    }
}
