package edu.harvard.we99.domain.results.analysis;

import curvefit.wrapper.SigmoidalCurveFitter;
import edu.harvard.we99.domain.ExperimentPoint;
import edu.harvard.we99.domain.FitParameter;
import edu.harvard.we99.domain.ParameterStatus;
import edu.harvard.we99.domain.results.ResultStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @author alan orcharton.
 *
 * Gets the parameters for curve fitting based on the experiment points
 */
public class CurveFitParametersFunction implements Function<List<ExperimentPoint>, List<FitParameter>> {

    private static final String[] targetParams = {"Slope","Min","Max","EC50"};
    public static final boolean USER_OUTLIER_DETECTION = true;

    public CurveFitParametersFunction() {

    }

    public static double[] convertTodouble(List<Double> points) {

        double[] groupResults = new double[points.size()];
        int i = 0;
        for (Double d : points) {
            groupResults[i++] = d;
        }

        return groupResults;
    }

    public static boolean isTargetParam(String candidate){
        boolean targetResult = false;
         for(String t : CurveFitParametersFunction.targetParams){
             if(t.equalsIgnoreCase(candidate)){
                 targetResult = true;
                 break;
             }
         }
        return targetResult;
    }

    @Override
    public List<FitParameter> apply(List<ExperimentPoint> experimentPoints) {

        Predicate<ExperimentPoint> includedPoints = ep -> ep.getResultStatus() == ResultStatus.INCLUDED;

        List<Double> xarray = experimentPoints.stream()
                    .filter(includedPoints)
                    .map(ExperimentPoint::getX).collect(Collectors.toList());
        List<Double> yarray = experimentPoints.stream()
                    .filter(includedPoints)
                    .map(ExperimentPoint::getY).collect(Collectors.toList());

        double[] xInput = convertTodouble(xarray);
        double[] yInput = convertTodouble(yarray);

        int numPoints = xInput.length;
        String[] exclusions = new String[numPoints];

        for(int i = 0; i < numPoints; i++) {
            exclusions[i] = "N";
        }

        SigmoidalCurveFitter sigmoidalFitter = new SigmoidalCurveFitter();

        HashMap<String,Object> fitResults = sigmoidalFitter.fit(xInput, yInput, exclusions, USER_OUTLIER_DETECTION);


        List<String> prarms = fitResults.keySet().stream()
                                                        .filter(CurveFitParametersFunction::isTargetParam)
                                                        .collect(Collectors.toList());

        //TODO: see if profs stuff mentions if the param is fixed of floated
        List<FitParameter> fitParameters = new ArrayList<>();
        for( String s : prarms){
            Double value = (Double) fitResults.get(s);
            if (value != null){
                fitParameters.add(new FitParameter(s,value, ParameterStatus.FLOAT));
            }
        }

        return fitParameters;
    }
}
