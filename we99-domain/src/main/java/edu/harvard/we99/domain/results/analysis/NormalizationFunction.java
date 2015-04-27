package edu.harvard.we99.domain.results.analysis;

import edu.harvard.we99.domain.results.WellResults;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

/**
 * @author mford
 */
public class NormalizationFunction implements Function<List<WellResults>,Void> {

    private final Function<List<Double>, List<Double>> function;

    public NormalizationFunction() {
        this.function = new CommonsMathNormalization();
    }

    @Override
    public Void apply(List<WellResults> wrList) {

        // collect the labels for all samples since we need to normalize each
        // of the samples with the other samples of its kind
        Set<String> labels = FunctionUtils.gatherSampleLabels().apply(wrList);

        for(String lbl : labels) {
            List<Double> input = new ArrayList<>();
            // gather all of the values for this label
            wrList.forEach(wr-> input.add(wr.getByLabel(lbl).getValue()));

            // normalize them
            List<Double> output = function.apply(input);

            // apply the normalized values back
            wrList.forEach(wr-> wr.getByLabel(lbl).setNormalized(output.remove(0)));
        }

        return null;
    }
}
