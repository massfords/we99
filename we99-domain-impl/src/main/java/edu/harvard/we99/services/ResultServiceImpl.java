package edu.harvard.we99.services;

import edu.harvard.we99.domain.Experiment;
import edu.harvard.we99.domain.Plate;
import edu.harvard.we99.domain.results.PlateResult;
import edu.harvard.we99.services.io.PlateResultCSVReader;
import edu.harvard.we99.services.storage.ExperimentStorage;
import edu.harvard.we99.services.storage.ResultStorage;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @author mford
 */
public class ResultServiceImpl implements ResultService {

    private final ResultStorage storage;
    private final ExperimentStorage expStorage;

    public ResultServiceImpl(ResultStorage storage, ExperimentStorage expStorage) {
        this.expStorage = expStorage;
        this.storage = storage;
    }

    @Override
    public PlateResult uploadResults(Long experimentId, Long plateId, InputStream csv) {
        // get the plate w/ the given id
        // read the csv into a PlateResult
        // assign the Plate / Experiment to the PlateResult
        // persist all to the storage

        Experiment experiment = expStorage.get(experimentId);

        Plate plate = null;
        for(Plate p : experiment.getPlates()) {
            if (p.getId().equals(plateId)) {
                plate = p;
                break;
            }
        }
        if (plate == null) throw new WebApplicationException(Response.status(404).build());

        PlateResultCSVReader reader = new PlateResultCSVReader() ;
        PlateResult pr = reader.read(new BufferedReader(new InputStreamReader(csv)));
        pr.setExperiment(experiment);
        pr.setPlate(plate);
        storage.create(pr);
        return pr;
    }
}
