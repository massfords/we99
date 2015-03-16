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
    private Long experimentId;
    private Long plateId;
    private Long resultId;

    public PlateResultResourceImpl(ResultStorage resultStorage) {
        this.resultStorage = resultStorage;
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

    @Override
    public void setExperimentId(Long experimentId) {
        this.experimentId = experimentId;
    }

    @Override
    public void setPlateId(Long plateId) {
        this.plateId = plateId;
    }

    @Override
    public void setResultId(Long resultId) {
        this.resultId = resultId;
    }
}
