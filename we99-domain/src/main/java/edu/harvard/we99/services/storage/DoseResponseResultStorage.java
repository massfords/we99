package edu.harvard.we99.services.storage;

import edu.harvard.we99.domain.Coordinate;
import edu.harvard.we99.domain.Dose;
import edu.harvard.we99.domain.ExperimentPoint;
import edu.harvard.we99.domain.FitParameter;
import edu.harvard.we99.domain.lists.DoseResponseResults;
import edu.harvard.we99.domain.results.DoseResponseResult;
import edu.harvard.we99.domain.results.ResultStatus;

import java.util.List;
import java.util.Set;


/**
 * @author alan orcharton
 */
public interface DoseResponseResultStorage extends CRUDStorage<DoseResponseResult> {

    void createAll(Long experimentId);
    Set<Long> getPlateIds(Long doseResponseId);
    Dose getDose(Long doseResponseId, Long doseId);
    DoseResponseResult getByCompoundName(String compoundName);
    void replaceExperimentPoints(Long doseResponseResultId,List<ExperimentPoint>newPoints);
    ExperimentPoint addExperimentPoint(Long doseResponseId, ExperimentPoint point);
    ExperimentPoint updateExperimentPoint(Long doseResponseId, ExperimentPoint type);
    void updateAllExperimentPoints(Long doseResponseId,List<ExperimentPoint> experimentPointTypes);
    DoseResponseResults listAll(Long experimentId, Integer page, Integer pageSize,
                   String typeAhead);
    DoseResponseResults getAll(Long experimentId);
    DoseResponseResult addFitParameter(Long doseResponseResultId, FitParameter parameter);
    DoseResponseResult updateCurveFitPoints(Long doseResponseId, DoseResponseResult type);

    void addWell(Long doseResponseId, Long wellId);
    void addWells(Long doseResponseId, List<Long> wells);
    void updateStatus(Long id, Coordinate coordinate, ResultStatus status);
}
