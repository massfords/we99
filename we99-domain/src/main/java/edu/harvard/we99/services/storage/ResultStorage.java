package edu.harvard.we99.services.storage;

import edu.harvard.we99.domain.Coordinate;
import edu.harvard.we99.domain.results.PlateResult;
import edu.harvard.we99.domain.results.PlateResultEntry;
import edu.harvard.we99.domain.results.ResultStatus;

import java.util.List;

/**
 * @author mford
 */
public interface ResultStorage extends CRUDStorage<PlateResult> {

    void updateStatus(Long id, Coordinate coordinate, ResultStatus status);

    List<PlateResultEntry> listAllByExperiment(Long experimentId);
}
