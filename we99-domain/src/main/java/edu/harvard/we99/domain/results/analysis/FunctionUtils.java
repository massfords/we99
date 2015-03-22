package edu.harvard.we99.domain.results.analysis;

import edu.harvard.we99.domain.results.WellResults;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

/**
 * @author mford
 */
public class FunctionUtils {
    private FunctionUtils() {}

    public static Function<List<WellResults>, Set<String>> gatherSampleLabels() {
        return wrList -> {
            Set<String> labels = new LinkedHashSet<>();
            wrList.forEach(wr->wr.getSamples().forEach(s->labels.add(s.getLabel())));
            return labels;
        };
    }
}
