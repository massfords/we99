package edu.harvard.we99.domain.results.analysis;

import edu.harvard.we99.domain.results.WellResults;
import org.apache.commons.math3.stat.StatUtils;
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;

import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * The ZeeFactorFunction is used to calculate the Z'(Z Prime) and Z Factor.
 * The only difference is that Z Prime uses the Positive and Negative control wells,
 * whereas Z Factor uses the Positive control wells and the sample compounds.
 * Change this by altering the filter between Negative control wells and sample wells.
 *
 * @author alan orcharton.
 */
public class ZeeFactorFunction implements Function<List<WellResults>,Double> {

    private final String label;
    private final Predicate<WellResults> wellFilter1;
    private final Predicate<WellResults> wellFilter2;

    public ZeeFactorFunction(String label,Predicate<WellResults> posWellFilter,Predicate<WellResults> sampWellFilter){
        this.label = label;
        this.wellFilter1 = posWellFilter;
        this.wellFilter2 = sampWellFilter;

    }

    private double[] getGroupResults(List<WellResults> group) {
        List<Double> collect = group.stream()
                .map(wr -> wr.getByLabel(label).getValue()).collect(Collectors.toList());
        double[] groupResults = new double[collect.size()];
        int i=0;
        for(Double d : collect) {
            groupResults[i++] = d;
        }

        return groupResults;

    }

    @Override
    public Double apply(List<WellResults> wrList) {

        StandardDeviation sd = new StandardDeviation();
        sd.setBiasCorrected(false);

        Function<double[], Double> mean = StatUtils::mean;
        Function<double[], Double> stdDev = sd::evaluate;

        List<WellResults> group1 = wrList.stream().filter(wellFilter1).collect(Collectors.toList());
        double[] group1Results = getGroupResults(group1);

        double group1Mean = mean.apply(group1Results);
        double group1StdDev = stdDev.apply(group1Results);

        List<WellResults> group2 = wrList.stream().filter(wellFilter2).collect(Collectors.toList());

        double[] group2Results = getGroupResults(group2);
        double group2Mean = mean.apply(group2Results);
        double group2StdDev = stdDev.apply(group2Results);

        double zeeFactorCalc = 1 - ( 3 * (group1StdDev + group2StdDev) / (Math.abs(group1Mean - group2Mean)));

        return (Double.isNaN(zeeFactorCalc) || Double.isInfinite(zeeFactorCalc))? null : zeeFactorCalc;

    }
}
