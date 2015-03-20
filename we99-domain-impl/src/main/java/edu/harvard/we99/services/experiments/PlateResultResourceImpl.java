package edu.harvard.we99.services.experiments;

import edu.harvard.we99.domain.Experiment;
import edu.harvard.we99.domain.Plate;
import edu.harvard.we99.domain.results.PlateResult;
import edu.harvard.we99.domain.results.StatusChange;
import edu.harvard.we99.services.io.PlateResultCSVReader;
import edu.harvard.we99.services.storage.ResultStorage;
import org.apache.commons.io.IOUtils;

import javax.persistence.PersistenceException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;

/**
 * @author mford
 */
public class PlateResultResourceImpl implements PlateResultResource {
    private final ResultStorage resultStorage;
    private Long experimentId;
    private Long plateId;

    public PlateResultResourceImpl(ResultStorage resultStorage) {
        this.resultStorage = resultStorage;
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
        pr.setPlate(new Plate().withId(plateId).withExperiment(new Experiment().withId(experimentId)));
        pr.setSource(source);
        return resultStorage.create(pr);
    }

    @Override
    public Response updateStatus(StatusChange statusChange) {
        resultStorage.updateStatus(plateId, statusChange.getCoordinate(), statusChange.getStatus());
        return Response.ok().build();
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
