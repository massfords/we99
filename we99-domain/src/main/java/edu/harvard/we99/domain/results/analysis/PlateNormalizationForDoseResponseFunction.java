package edu.harvard.we99.domain.results.analysis;

import edu.harvard.we99.domain.Coordinate;
import edu.harvard.we99.domain.Plate;
import edu.harvard.we99.domain.WellType;
import edu.harvard.we99.domain.results.WellResults;

import java.util.*;
import java.util.function.Function;

/**
 * @author alan orcharton
 */
public class PlateNormalizationForDoseResponseFunction implements Function<List<WellResults>,List<WellResults>> {

    private final Map<Coordinate,WellType> wellTypes = new HashMap<>();

    public PlateNormalizationForDoseResponseFunction(Plate plate) {
        plate.getWells().values().forEach(w->wellTypes.put(w.getCoordinate(), w.getType()));
        assert !wellTypes.isEmpty();
    }

    @Override
    public List<WellResults> apply(List<WellResults> wrList) {

        // todo - do we need this call to gatherSampleLabels?
        Set<String> labels = FunctionUtils.gatherSampleLabels().apply(wrList);
        String label = "";
        NormalizeForPercentEffectFunction nef = new NormalizeForPercentEffectFunction(label,
                wellResults -> wellTypes.get(wellResults.getCoordinate()) == WellType.POSITIVE,
                wellResults -> wellTypes.get(wellResults.getCoordinate()) == WellType.NEGATIVE,
                wellResults -> wellTypes.get(wellResults.getCoordinate()) == WellType.COMP);

        List<WellResults> normalized = nef.apply(wrList);


        return normalized;
    }
}
