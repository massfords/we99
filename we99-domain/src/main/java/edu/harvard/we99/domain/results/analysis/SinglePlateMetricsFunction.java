package edu.harvard.we99.domain.results.analysis;

import edu.harvard.we99.domain.results.WellResults;

import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @author mford
 */
public class SinglePlateMetricsFunction implements Function<List<WellResults>,Double> {

    private final String label;
    private final Predicate<WellResults> filter;
    private final Function<double[],Double> function;

    public SinglePlateMetricsFunction(String label, Function<double[], Double> function,
                                      Predicate<WellResults> filter) {
        this.label = label;
        this.filter = filter;
        this.function = function;
    }

    @Override
    public Double apply(List<WellResults> wrList) {

        List<WellResults> accepted = wrList.stream().filter(filter).collect(Collectors.toList());

        List<Double> collect = accepted.stream()
                .map(wr -> wr.getByLabel(label).getNormalized()).collect(Collectors.toList());
        double[] doubles = new double[collect.size()];
        int i=0;
        for(Double d : collect) {
            doubles[i++] = d;
        }

        Double d = function.apply(doubles);

        return Double.isNaN(d)? null : d;
    }
}
