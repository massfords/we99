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
import edu.harvard.we99.services.io.PlateResultCSVReader;
import edu.harvard.we99.services.storage.PlateStorage;
import edu.harvard.we99.services.storage.ResultStorage;
import org.apache.commons.io.IOUtils;

import javax.persistence.PersistenceException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author mford
 */
public class PlateResultResourceImpl implements PlateResultResource {
    private final ResultStorage resultStorage;
    private final PlateStorage plateStorage;
    private Long experimentId;
    private Long plateId;

    public PlateResultResourceImpl(ResultStorage resultStorage, PlateStorage plateStorage) {
        this.resultStorage = resultStorage;
        this.plateStorage = plateStorage;
    }

    @Override
    public PlateResult get() {
        PlateResult plateResult = getPlateResult();
        if (!plateResult.getPlate().getExperiment().getId().equals(experimentId)) {
            throw new WebApplicationException(Response.status(404).build());
        }
        return plateResult;
    }

    @Override
    public PlateResult uploadResults(InputStream csv) {
        // get the plate w/ the given id
        // read the csv into a PlateResult
        // assign the Plate / Experiment to the PlateResult
        // persist all to the storage

        String source;
        try {
            source = IOUtils.toString(csv);
        } catch (IOException e) {
            throw new WebApplicationException(Response.serverError().build());
        }

        PlateResultCSVReader reader = new PlateResultCSVReader();
        PlateResult pr = reader.read(new BufferedReader(new StringReader(source)));
        pr.setPlate(new Plate().withId(plateId).withExperiment(
                new Experiment().withId(experimentId)));
        pr.setSource(source);
        pr.setMetrics(compute(pr));
        return resultStorage.create(pr);
    }

    @Override
    public PlateResult updateStatus(StatusChange statusChange) {
        resultStorage.updateStatus(plateId, statusChange.getCoordinate(), statusChange.getStatus());

        PlateResult plateResult = resultStorage.get(plateId);
        plateResult.setMetrics(compute(plateResult));

        PlateResult updated = resultStorage.update(plateId, plateResult);

        return updated;
    }

    private Map<String,PlateMetrics> compute(PlateResult result) {

        Plate plate = plateStorage.get(plateId);

        Map<Coordinate, WellResults> wellResults = result.getWellResults();

        // normalize all of the wells
        NormalizationFunction nf = new NormalizationFunction();
        List<WellResults> wrList = new ArrayList<>(wellResults.values());
        nf.apply(wrList);

        // compute the average of the positive controls, negative controls, z, and z'
        List<PlateMetrics> list = new PlateMetricsFunction(plate).apply(wrList);
        Map<String,PlateMetrics> metrics = new HashMap<>();
        list.forEach(pm -> metrics.put(pm.getLabel(), pm));
        return metrics;
    }

    @Override
    public void setExperimentId(Long experimentId) {
        this.experimentId = experimentId;
    }

    @Override
    public void setPlateId(Long plateId) {
        this.plateId = plateId;
    }

    private PlateResult getPlateResult() {
        PlateResult plateResult;
        try {
            plateResult = resultStorage.get(plateId);

        } catch(PersistenceException e) {
            throw new WebApplicationException(Response.status(404).build());
        }
        return plateResult;
    }
}
