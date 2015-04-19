package edu.harvard.we99.domain.results.analysis;

import edu.harvard.we99.domain.ExperimentPoint;
import edu.harvard.we99.domain.results.DoseResponseResult;
import edu.harvard.we99.domain.results.WellResults;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * Created by HUID 70786729 on 4/19/15.
 */
public class ExperimentPointsFunction implements Function<Map<Long,WellResults>, List<ExperimentPoint>> {

    private final List<ExperimentPoint> currentPoints;

    private  ExperimentPointsFunction(List<ExperimentPoint> currentPoints){
            this.currentPoints = currentPoints;

    }


    @Override
    public List<ExperimentPoint> apply(Map<Long, WellResults> longWellResultsMap) {
        return null;
    }
}
