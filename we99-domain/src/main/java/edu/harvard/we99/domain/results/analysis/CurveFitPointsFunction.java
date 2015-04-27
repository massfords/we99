package edu.harvard.we99.domain.results.analysis;

import edu.harvard.we99.domain.CurveFitPoint;
import edu.harvard.we99.domain.ExperimentPoint;
import edu.harvard.we99.domain.FitEquation;
import edu.harvard.we99.domain.FitParameter;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author alan orcharton
 *
 * This function takes the Experiment Points, finds the max and min points
 * and generate new points between the max and min that have been fitted to
 * the Best Fit curve. These points can be used for plotting the best fit
 * curve.
 *
 * The constructor takes the fit parameters and the equation to draw the curve
 *
 */
public class CurveFitPointsFunction implements Function<List<ExperimentPoint>, List<CurveFitPoint>> {

    private final List<FitParameter> fitParameters;
    private final int numPoints;
    // todo - what's up w/ these unused fields?
    private Function<List<Double>,List<Double>> curveFunction;
    private static final Double DEFAULT_MIN = 1.0E-10;
    private static final Double DEFAULT_MAX = 6.309573444801929E-5;
    private static final FitEquation fitequation = FitEquation.HILLEQUATION;

    public CurveFitPointsFunction (List<FitParameter> fitParameters, int numPoints,
                                   FitEquation fitEquation){
        this.fitParameters = fitParameters;
        this.numPoints = numPoints;
        //this.fitequation = fitEquation;

    }

     private List<CurveFitPoint> createCurvePoints(List<Double> xs, List<Double> ys){
         List<CurveFitPoint> curvePointsList = new ArrayList<>();
         for(int i=0; i< xs.size(); i++){
               CurveFitPoint cfp = new CurveFitPoint().setX(Math.log10(xs.get(i)));
               cfp.setY(ys.get(i));
               cfp.setSequenceNumber(i);
               curvePointsList.add(cfp);
           }
         return curvePointsList;
     }

    @Override
    public List<CurveFitPoint> apply(List<ExperimentPoint> experimentPoints) {

        Optional<Double> max = experimentPoints.stream()
                                    .map(ExperimentPoint::getX)
                                    .reduce(Double::max);


        Optional<Double> min = experimentPoints.stream()
                .map(ExperimentPoint::getX)
                .reduce(Double::min);

        Double maxlog10 = Math.log10(max.orElse(DEFAULT_MAX));
        Double minlog10 = Math.log10(min.orElse(DEFAULT_MIN));

        Double logStep = (maxlog10 - minlog10) / (double)numPoints;

        //generate points
        List<Double> xlogpoints = new ArrayList<>();
        Double xlogpoint = minlog10;
        xlogpoints.add(xlogpoint);
        for(int x=0; x < numPoints; x++){
            xlogpoint = xlogpoint + logStep;
            xlogpoints.add(xlogpoint);
        }

        List<Double> xpoints = xlogpoints.stream()
                    .map(xlog -> Math.pow(10,xlog))
                    .collect(Collectors.toList());


        //if(fitequation == FitEquation.HILLEQUATION)
        Map<String,Double> fitParameterMap = new HashMap<>();
        for(FitParameter f : fitParameters){
            fitParameterMap.put(f.getName(),f.getValue());

        }

        HillEquationFunction func = new HillEquationFunction(fitParameterMap.get("Max"),fitParameterMap.get("Min"),
                fitParameterMap.get("Slope"),fitParameterMap.get("EC50"));

        List<Double> ypoints = func.apply(xpoints);

        List<CurveFitPoint> curveFitPoints = createCurvePoints(xpoints,ypoints);

        return curveFitPoints;
    }
}
