package edu.harvard.we99.domain.results.analysis;

import edu.harvard.we99.domain.results.Sample;
import edu.harvard.we99.domain.results.WellResults;
import org.apache.commons.math3.stat.StatUtils;

import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @author alan orcharton
 *
 * Returns a list of compound WellResults normalized against positive and negative control wells.
 *
 */
public class NormalizeForPercentEffectFunction implements Function<List<WellResults>,List<WellResults>> {

    private final String label;
    private final Predicate<WellResults> positiveCtrlFilter;
    private final Predicate<WellResults> negativeCtrlFilter;
    private final Predicate<WellResults> compoundFilter;

    public NormalizeForPercentEffectFunction(String label, Predicate<WellResults> positiveCtrlFilter,
                                             Predicate<WellResults> negativeCtrlFilter, Predicate<WellResults>compoundFilter) {

        this.positiveCtrlFilter = positiveCtrlFilter;
        this.negativeCtrlFilter = negativeCtrlFilter;
        this.compoundFilter = compoundFilter;
        this.label = label;

    }



    //takes a list of well results gets the value and convert to a double[]
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
    public List<WellResults> apply(List<WellResults> wrList) {

        Function<double[], Double> mean = StatUtils::mean;

        //filter positive controls
        List<WellResults> group1 = wrList.stream().filter(positiveCtrlFilter).collect(Collectors.toList());
        double[] group1Results = getGroupResults(group1);

        //mean of positive controls
        double positiveMean = mean.apply(group1Results);

        //filter negative controls
        List<WellResults> group2 = wrList.stream().filter(negativeCtrlFilter).collect(Collectors.toList());
        double[] group2Results = getGroupResults(group2);

        //mean of positive controls
        double negativeMean = mean.apply(group2Results);

        //filter compounds and map normalized values
        List<WellResults> normalizedCompounds = wrList.stream()
                                                .filter(compoundFilter)
                                                .map(wr -> {
                                                    Sample s = wr.getByLabel(label);
                                                    Double data = s.getValue();
                                                    Double normalized = 100 * (((data - negativeMean) / (positiveMean - negativeMean)));
                                                    s.setNormalized(normalized);
                                                    return wr;
                                                })
                                                .collect(Collectors.toList());


        return normalizedCompounds;
    }
}
