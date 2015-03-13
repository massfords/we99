package edu.harvard.we99.services.experiments;

import edu.harvard.we99.domain.lists.PlateResults;
import edu.harvard.we99.services.storage.ResultStorage;

/**
 * @author mford
 */
public class PlateResultsResourceImpl implements PlateResultsResource {

    private final Long experimentId;
    private final Long plateId;
    private final ResultStorage resultStorage;

    public PlateResultsResourceImpl(Long experimentId, Long plateId, ResultStorage resultStorage) {
        this.plateId = plateId;
        this.resultStorage = resultStorage;
        this.experimentId = experimentId;
    }

    @Override
    public PlateResults listByPlate() {
        return new PlateResults(resultStorage.listAllByPlate(experimentId, plateId));
    }

    @Override
    public PlateResultResource getPlateResult(Long resultId) {
        return new PlateResultResourceImpl(experimentId, plateId,
                resultId, resultStorage);
    }
}
