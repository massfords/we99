package edu.harvard.we99.services.storage;

import edu.harvard.we99.domain.Coordinate;
import edu.harvard.we99.domain.lists.PlateResults;
import edu.harvard.we99.domain.results.PlateResult;
import edu.harvard.we99.domain.results.ResultStatus;

import java.util.List;

/**
 * @author mford
 */
public interface ResultStorage extends CRUDStorage<PlateResult> {

    void updateStatus(Long id, Coordinate coordinate, ResultStatus status);

    PlateResults listAllByExperiment(Long experimentId, Integer page,
                                     Integer pageSize, String typeAhead);

    PlateResult getByPlateId(Long plateId);

    void create(List<PlateResult> results);

    void fullMonty(List<PlateResult> results);
}
