package edu.harvard.we99.services.experiments;

import edu.harvard.we99.domain.Compound;
import edu.harvard.we99.domain.Experiment;
import edu.harvard.we99.domain.ExperimentStatus;
import edu.harvard.we99.domain.Plate;
import edu.harvard.we99.domain.PlateType;
import edu.harvard.we99.domain.lists.PlateResults;
import edu.harvard.we99.domain.results.PlateResult;
import edu.harvard.we99.services.BaseRESTServiceImpl;
import edu.harvard.we99.services.io.PlateCSVReader;
import edu.harvard.we99.services.io.PlateWithOptionalResults;
import edu.harvard.we99.services.storage.CompoundStorage;
import edu.harvard.we99.services.storage.ExperimentStorage;
import edu.harvard.we99.services.storage.ResultStorage;
import edu.harvard.we99.util.JacksonUtil;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Generated;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static edu.harvard.we99.services.experiments.PlateResultResourceImpl.compute;

/**
 * @author mford
 */
public abstract class ExperimentResourceImpl extends BaseRESTServiceImpl<Experiment>  implements ExperimentResource {

    private static final Logger log = LoggerFactory.getLogger(ExperimentResourceImpl.class);

    private Long id;
    private final ResultStorage resultStorage;
    private final CompoundStorage compoundStorage;
    private Experiment experiment;


    public ExperimentResourceImpl(ExperimentStorage storage,
                                  ResultStorage resultStorage,
                                  CompoundStorage compoundStorage) {
        super(storage);
        this.resultStorage = resultStorage;
        this.compoundStorage = compoundStorage;
    }

    @Override
    public Experiment get() {
        if (experiment == null) {
            experiment = super.get(id);
        }
        return experiment;
    }

    @Override
    public Experiment update(Experiment experiment) {
        return super.update(id, experiment);
    }

    @Override
    public Experiment publish() {
        try {
            Experiment experiment = get();
            if (experiment.getStatus() != ExperimentStatus.UNPUBLISHED) {
                throw new WebApplicationException(Response.status(409).build());
            }
            ExperimentStorage es = (ExperimentStorage) this.storage;
            return es.publish(experiment);
        } catch(Exception e) {
            log.error("error publising experiment {}", id);
            throw new WebApplicationException(Response.serverError().build());
        }
    }

    @Override
    public Response delete() {
        deleteImpl(id);
        return Response.ok().build();
    }

    @Override
    public MemberResource getMembers() {
        MemberResource mr = createMemberResource();
        mr.setId(id);
        return mr;
    }

    @Override
    public PlatesResource getPlates() {
        Experiment experiment = get();
        PlatesResource pr = createPlatesResource();
        pr.setExperiment(experiment);
        return pr;
    }

    @Override
    public DoseResponseResource getDoseResponses() {
        Experiment experiment = get();
        DoseResponseResource dr = createDoseResponseResource();
        dr.setExperiment(experiment);
        return dr;
    }


    @Override
    public PlateResults listResults(Integer page, Integer pageSize, String typeAhead) {
        try {
            return resultStorage.listAllByExperiment(id, page, pageSize, typeAhead);
        } catch(Exception e) {
            log.error("error listing results. Page {}, pageSize {}, query {}",
                    page, pageSize, typeAhead, e);
            throw new WebApplicationException(Response.serverError().build());
        }
    }

    @Override
    public Response stringMonty(String plateType, InputStream csv) {
        assert plateType != null;
        try {
            return fullMonty(JacksonUtil.fromString(plateType, PlateType.class), csv);
        } catch (Exception e) {
            throw new WebApplicationException(Response.serverError().build());
        }
    }

    @Override
    public Response fullMonty(PlateType plateType, InputStream csv) {
        // read the csv into a List<PlateResult>
        // assign the Plate / Experiment to the PlateResult
        // persist all to the storage

        assert plateType != null;

        String source;
        try {
            source = IOUtils.toString(csv);

            PlateCSVReader reader = new PlateCSVReader();
            List<PlateWithOptionalResults> list = reader.read(new StringReader(source));

            log.debug("Full monty will load {} plates with results", list.size());

            Set<Compound> compoundSet = new LinkedHashSet<>();
            for(PlateWithOptionalResults pwoList : list) {
                pwoList.getPlate()
                        .getWells()
                        .values()
                        .stream()
                        .forEach(
                                w -> w.getContents()
                                        .stream()
                                        .filter(d -> d.getCompound() != null)
                                        .forEach(d -> compoundSet.add(d.getCompound())));
            }

            Map<Compound, Long> compoundLongMap = compoundStorage.resolveIds(compoundSet);

            for(PlateWithOptionalResults pwoList : list) {
                pwoList.getPlate()
                        .getWells()
                        .values()
                        .stream()
                        .forEach(
                                w -> w.getContents()
                                        .stream()
                                        .filter(d -> d.getCompound() != null)
                                        .forEach(d -> d.getCompound().setId(compoundLongMap.get(d.getCompound()))));
            }

            for(PlateWithOptionalResults pwithResult : list) {
                PlateResult results = pwithResult.getResults();
                Plate plate = pwithResult.getPlate();
                plate.setName(UUID.randomUUID().toString());
                plate.setPlateType(plateType);
                plate.setExperimentId(experiment.getId());
                pwithResult.getResults().setPlate(plate);
                results.setMetrics(compute(results, plate));
            }

            List<PlateResult> resultList = list
                    .stream()
                    .map(PlateWithOptionalResults::getResults)
                    .collect(Collectors.toList());
            resultStorage.fullMonty(resultList);
            return Response.ok().build();
        } catch (IOException e) {
            log.error("error parsing results csv", e);
            throw new WebApplicationException(Response.serverError().build());
        } catch (WebApplicationException e) {
            throw e;
        } catch (Exception e) {
            log.error("error inserting results csv", e);
            throw new WebApplicationException(Response.serverError().build());
        }
    }

    protected abstract PlatesResource createPlatesResource();

    protected abstract DoseResponseResource createDoseResponseResource();

    protected abstract MemberResource createMemberResource();


    @Generated(value = "generated by IDE")
    public Long getId() {
        return id;
    }

    @Generated(value = "generated by IDE")
    public void setId(Long id) {
        this.id = id;
    }

    @Generated("generated by IDE")
    public Experiment getExperiment() {
        return experiment;
    }
}
