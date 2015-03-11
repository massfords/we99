package edu.harvard.we99.services;

import edu.harvard.we99.domain.Experiment;
import edu.harvard.we99.domain.Plate;
import edu.harvard.we99.domain.lists.PlateResultEntries;
import edu.harvard.we99.domain.lists.PlateResults;
import edu.harvard.we99.domain.results.PlateResult;
import edu.harvard.we99.domain.results.StatusChange;
import edu.harvard.we99.security.UserContextProvider;
import edu.harvard.we99.services.io.PlateResultCSVReader;
import edu.harvard.we99.services.storage.PlateStorage;
import edu.harvard.we99.services.storage.ResultStorage;
import org.apache.commons.io.IOUtils;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;

/**
 * @author mford
 */
public class ResultServiceImpl implements ResultService {

    private final ResultStorage storage;
    private final PlateStorage plateStorage;
    private final UserContextProvider ucp;

    public ResultServiceImpl(UserContextProvider ucp,
                             ResultStorage storage,
                             PlateStorage plateStorage) {
        this.ucp = ucp;
        this.storage = storage;
        this.plateStorage = plateStorage;
    }

    @Override
    public PlateResult get(Long experimentId, Long plateId, Long id) {
        ucp.assertCallerIsMember(experimentId);
        PlateResult plateResult = storage.get(id);
        // verify that the plateid and experiment id are what we expect
        if (!plateResult.getPlate().getId().equals(plateId)) {
            throw new WebApplicationException(Response.status(404).build());
        }
        if (!plateResult.getPlate().getExperiment().getId().equals(experimentId)) {
            throw new WebApplicationException(Response.status(404).build());
        }
        return plateResult;
    }

    @Override
    public PlateResults listByPlate(Long experimentId, Long plateId) {
        ucp.assertCallerIsMember(experimentId);
        return new PlateResults(storage.listAllByPlate(experimentId, plateId));
    }

    @Override
    public PlateResultEntries listByExperiment(Long experimentId) {
        ucp.assertCallerIsMember(experimentId);
        return new PlateResultEntries(storage.listAllByExperiment(experimentId));
    }

    @Override
    public PlateResult uploadResults(Long experimentId, Long plateId, InputStream csv) {
        ucp.assertCallerIsMember(experimentId);
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
        return storage.create(pr);
    }

    @Override
    public Response updateStatus(Long experimentId, Long plateId, Long resultId,
                                 StatusChange statusChange) {
        ucp.assertCallerIsMember(experimentId);
        storage.updateStatus(resultId, statusChange.getCoordinate(), statusChange.getStatus());
        return Response.ok().build();
    }
}