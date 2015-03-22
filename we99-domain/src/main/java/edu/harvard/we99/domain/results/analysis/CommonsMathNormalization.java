package edu.harvard.we99.domain.results.analysis;

import org.apache.commons.math3.stat.StatUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

/**
 * @author mford
 */
public class CommonsMathNormalization implements Function<List<Double>, List<Double>> {
    @Override
    public List<Double> apply(List<Double> doubles) {
        double[] samples = new double[doubles.size()];
        int i=0;

        for(Double d : doubles) {
            samples[i++] = d;
        }
        double[] normalized = StatUtils.normalize(samples);

        List<Double> list = new ArrayList<>();
        Arrays.stream(normalized).forEach(list::add);
        return list;
    }
}
