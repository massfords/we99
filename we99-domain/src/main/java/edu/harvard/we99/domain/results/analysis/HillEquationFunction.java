package edu.harvard.we99.domain.results.analysis;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author alan orcharton
 */
public class HillEquationFunction implements Function<List<Double>,List<Double>> {

    Double max;
    Double min;
    Double slope;
    Double logEC50;

    public HillEquationFunction(Double max, Double min, Double slope, Double ec50){

        this.max = max;
        this.min = min;
        this.slope = slope;
        this.logEC50 = ec50;  //Math.log10(ec50);

    }



    @Override
    public List<Double> apply(List<Double> xPoints) {

        List <Double> responses;
        if( this.min == null || this.max == null || this.slope == null || this.logEC50 == null){

            responses = xPoints.stream().map(x -> 0.0).collect(Collectors.toList());
        }
        else {
           responses = xPoints.stream().map(Math::log10)
                    .map(x -> min + (max - min) / (1 + Math.pow(10, ((logEC50 - x) * slope))))
                    .collect(Collectors.toList());
        }
       // Double logX = Math.log10(aDouble);
       // Double response =  min + (max - min) / (1 + Math.pow(10,((logEC50-logX)*slope)));
                //response = bottom + (top-bottom)/(1+10^((logEC50-log(x))*slope))
        return responses;
    }
}
