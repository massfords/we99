package edu.harvard.we99.services.experiments;

import edu.harvard.we99.domain.Coordinate;
import edu.harvard.we99.domain.Experiment;
import edu.harvard.we99.domain.Plate;
import edu.harvard.we99.domain.results.PlateMetrics;
import edu.harvard.we99.domain.results.PlateResult;
import edu.harvard.we99.domain.results.StatusChange;
import edu.harvard.we99.domain.results.WellResults;
import edu.harvard.we99.domain.results.analysis.NormalizationFunction;
import edu.harvard.we99.domain.results.analysis.PlateMetricsFunction;
import edu.harvard.we99.services.experiments.internal.PlateResultResourceInternal;
import edu.harvard.we99.services.io.MatrixParser;
import edu.harvard.we99.services.io.PlateResultCSVReader;
import edu.harvard.we99.services.io.PlateResultsReader;
import edu.harvard.we99.services.io.SinglePlateResultCollector;
import edu.harvard.we99.services.storage.PlateStorage;
import edu.harvard.we99.services.storage.ResultStorage;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Generated;
import javax.persistence.PersistenceException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author mford
 */
public class PlateResultResourceImpl implements PlateResultResourceInternal {

    private static final Logger log = LoggerFactory.getLogger(PlatesResourceImpl.class);

    private final ResultStorage resultStorage;
    private final PlateStorage plateStorage;
    private Experiment experiment;
    private Long plateId;

    public PlateResultResourceImpl(ResultStorage resultStorage, PlateStorage plateStorage) {
        this.resultStorage = resultStorage;
        this.plateStorage = plateStorage;
    }

    @Override
    public PlateResult get() {
        PlateResult plateResult = getPlateResult();
        if (plateResult == null || !plateResult.getPlate().getExperimentId().equals(experiment.getId())) {
            throw new WebApplicationException(Response.status(404).build());
        }
        return plateResult;
    }

    @Override
    public PlateResult uploadResults(String format, InputStream csv) {
        // get the plate w/ the given id
        // read the csv into a PlateResult
        // assign the Plate / Experiment to the PlateResult
        // persist all to the storage

        String source;
        try {
            source = IOUtils.toString(csv);

            PlateResultsReader reader = createReader(format);
            SinglePlateResultCollector collector = new SinglePlateResultCollector();
            reader.read(new BufferedReader(new StringReader(source)), collector);
            PlateResult pr = collector.getResults().get(0);
            pr.setPlate(new Plate().setId(plateId).setExperimentId(experiment.getId()));
            pr.setSource(source);
            pr.setMetrics(compute(pr));
            return resultStorage.create(pr);
        } catch (IOException e) {
            log.error("error parsing results csv", e);
            throw new WebApplicationException(Response.serverError().build());
        } catch (Exception e) {
            log.error("error inserting results csv", e);
            throw new WebApplicationException(Response.serverError().build());
        }

    }

    public static PlateResultsReader createReader(String format) {
        return "matrix".equalsIgnoreCase(format)? new MatrixParser() : new PlateResultCSVReader();
    }

    @Override
    public PlateResult updateStatus(StatusChange statusChange) {
        try {
            resultStorage.updateStatus(plateId, statusChange.getCoordinate(), statusChange.getStatus());

            PlateResult plateResult = resultStorage.get(plateId);
            plateResult.setMetrics(compute(plateResult));

            PlateResult updated = resultStorage.update(plateId, plateResult);

            return updated;
        } catch(Exception e) {
            log.error("error updating well status {}", statusChange, e);
            throw new WebApplicationException(Response.serverError().build());
        }
    }

    private List<PlateMetrics> compute(PlateResult result) {

        Plate plate = plateStorage.get(plateId);

        return compute(result, plate);
    }

    public static List<PlateMetrics> compute(PlateResult result, Plate plate) {
        Map<Coordinate, WellResults> wellResults = result.getWellResults();

        // normalize all of the wells
        NormalizationFunction nf = new NormalizationFunction();
        List<WellResults> wrList = new ArrayList<>(wellResults.values());
        nf.apply(wrList);

        // compute the average of the positive controls, negative controls, z, and z'
        List<PlateMetrics> list = new PlateMetricsFunction(plate).apply(wrList);
        return list;
    }

    @Override
    public void setPlateId(Long plateId) {
        this.plateId = plateId;
    }

    private PlateResult getPlateResult() {

        PlateResult plateResult;
        try {
            plateResult = resultStorage.getByPlateId(plateId);

        } catch(PersistenceException e) {
            throw new WebApplicationException(Response.status(404).build());
        }
        return plateResult;
    }

    @Override
    @Generated("generated by IDE")
    public Experiment getExperiment() {
        return experiment;
    }

    @Override
    @Generated("generated by IDE")
    public void setExperiment(Experiment experiment) {
        this.experiment = experiment;
    }
}
