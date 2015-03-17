package edu.harvard.we99.services.experiments;

import edu.harvard.we99.domain.lists.PlateResults;
import edu.harvard.we99.services.storage.ResultStorage;

/**
 * @author mford
 */
public abstract class PlateResultsResourceImpl implements PlateResultsResource {

    private Long experimentId;
    private Long plateId;
    private final ResultStorage resultStorage;

    public PlateResultsResourceImpl(ResultStorage resultStorage) {
        this.resultStorage = resultStorage;
    }

    @Override
    public PlateResults listByPlate() {
        return new PlateResults(resultStorage.listAllByPlate(experimentId, plateId));
    }

    @Override
    public PlateResultResource getPlateResult(Long resultId) {
        PlateResultResource prr = createPlateResultResource();
        prr.setExperimentId(experimentId);
        prr.setPlateId(plateId);
        prr.setResultId(resultId);
        return prr;
    }

    protected abstract PlateResultResource createPlateResultResource();

    @Override
    public void setExperimentId(Long experimentId) {
        this.experimentId = experimentId;
    }

    @Override
    public void setPlateId(Long plateId) {
        this.plateId = plateId;
    }
}
