package edu.harvard.we99.services.storage;

import edu.harvard.we99.domain.Coordinate;
import edu.harvard.we99.domain.ExperimentPoint;
import edu.harvard.we99.domain.results.DoseResponseResult;
import edu.harvard.we99.domain.results.ResultStatus;

import java.util.List;


/**
 * @author alan orcharton
 */
public interface DoseResponseResultStorage extends CRUDStorage<DoseResponseResult> {

    ExperimentPoint addExperimentPoint(Long doseResponseId, ExperimentPoint point);
    ExperimentPoint updateExperimentPoint(Long doseResponseId, ExperimentPoint type);
    void updateAllExperimentPoints(Long doseResponseId,List<ExperimentPoint> experimentPointTypes);

    void addWell(Long doseResponseId, Long wellId);
    void addWells(Long doseResponseId, List<Long> wells);
    void updateStatus(Long id, Coordinate coordinate, ResultStatus status);
}