package edu.harvard.we99.services.storage;

import edu.harvard.we99.domain.Coordinate;
import edu.harvard.we99.domain.lists.PlateResults;
import edu.harvard.we99.domain.results.PlateResult;
import edu.harvard.we99.domain.results.ResultStatus;

/**
 * @author mford
 */
public interface ResultStorage extends CRUDStorage<PlateResult> {

    void updateStatus(Long id, Coordinate coordinate, ResultStatus status);

    PlateResults listAllByExperiment(Long experimentId, Integer page,
                                     Integer pageSize, String typeAhead);
}
