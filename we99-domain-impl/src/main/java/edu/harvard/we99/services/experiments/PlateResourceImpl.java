package edu.harvard.we99.services.experiments;

import edu.harvard.we99.domain.Plate;
import edu.harvard.we99.services.storage.PlateStorage;

import javax.ws.rs.core.Response;

/**
 * @author mford
 */
public abstract class PlateResourceImpl implements PlateResource {

    private Long experimentId;
    private Long plateId;
    private final PlateStorage plateStorage;

    public PlateResourceImpl(PlateStorage plateStorage) {
        this.plateStorage = plateStorage;
    }

    @Override
    public Plate get() {
        return plateStorage.get(plateId);
    }

    @Override
    public Plate update(Plate plate) {
        return plateStorage.update(plateId, plate);
    }

    @Override
    public Response delete() {
        plateStorage.delete(plateId);
        return Response.ok().build();
    }

    @Override
    public PlateResultResource getPlateResult() {
        PlateResultResource prr = createPlateResult();
        prr.setExperimentId(experimentId);
        prr.setPlateId(plateId);
        return prr;
    }

    protected abstract PlateResultResource createPlateResult();


    @Override
    public void setExperimentId(Long experimentId) {
        this.experimentId = experimentId;
    }

    @Override
    public void setPlateId(Long plateId) {
        this.plateId = plateId;
    }
}
