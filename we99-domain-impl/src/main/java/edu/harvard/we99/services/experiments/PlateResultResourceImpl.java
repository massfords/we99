package edu.harvard.we99.services.experiments;

import edu.harvard.we99.domain.results.PlateResult;
import edu.harvard.we99.domain.results.StatusChange;
import edu.harvard.we99.services.storage.ResultStorage;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

/**
 * @author mford
 */
public class PlateResultResourceImpl implements PlateResultResource {
    private final ResultStorage resultStorage;
    private final Long experimentId;
    private final Long plateId;
    private final Long resultId;

    public PlateResultResourceImpl(Long experimentId, Long plateId, Long resultId,
                                   ResultStorage resultStorage) {
        this.resultStorage = resultStorage;
        this.resultId = resultId;
        this.experimentId = experimentId;
        this.plateId = plateId;
    }

    @Override
    public PlateResult get() {
        PlateResult plateResult = resultStorage.get(resultId);
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
    public Response updateStatus(StatusChange statusChange) {
        resultStorage.updateStatus(resultId, statusChange.getCoordinate(), statusChange.getStatus());
        return Response.ok().build();
    }
}
