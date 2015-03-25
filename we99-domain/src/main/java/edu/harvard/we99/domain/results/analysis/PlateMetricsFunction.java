package edu.harvard.we99.domain.results.analysis;

import edu.harvard.we99.domain.Coordinate;
import edu.harvard.we99.domain.Plate;
import edu.harvard.we99.domain.WellType;
import edu.harvard.we99.domain.results.PlateMetrics;
import edu.harvard.we99.domain.results.WellResults;
import org.apache.commons.math3.stat.StatUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

/**
 * @author mford
 */
public class PlateMetricsFunction implements Function<List<WellResults>, List<PlateMetrics>> {

    private final Map<Coordinate,WellType> wellTypes = new HashMap<>();

    public PlateMetricsFunction(Plate plate) {
        plate.getWells().values().forEach(w->wellTypes.put(w.getCoordinate(), w.getType()));
        assert !wellTypes.isEmpty();
    }

    @Override
    public List<PlateMetrics> apply(List<WellResults> wrList) {
        List<PlateMetrics> list = new ArrayList<>();

        Set<String> labels = FunctionUtils.gatherSampleLabels().apply(wrList);
        for(String s : labels) {
            PlateMetrics pm = new PlateMetrics();
            pm.setLabel(s);

            Function<double[], Double> mean = StatUtils::mean;

            pm.setAvgPositive(new SinglePlateMetricsFunction(s,
                    mean,
                    wellResults -> wellTypes.get(wellResults.getCoordinate()) == WellType.POSITIVE).apply(wrList));
            pm.setAvgNegative(new SinglePlateMetricsFunction(s,
                    mean,
                    wellResults -> wellTypes.get(wellResults.getCoordinate()) == WellType.NEGATIVE).apply(wrList));

            pm.setZee(new ZeeFactorFunction(s,
                    wellResults -> wellTypes.get(wellResults.getCoordinate()) == WellType.POSITIVE,
                    wellResults -> wellTypes.get(wellResults.getCoordinate()) == WellType.COMP).apply(wrList));

            pm.setZeePrime(new ZeeFactorFunction(s,
                    wellResults -> wellTypes.get(wellResults.getCoordinate()) == WellType.POSITIVE,
                    wellResults -> wellTypes.get(wellResults.getCoordinate()) == WellType.NEGATIVE).apply(wrList));

            list.add(pm);
        }

        return list;
    }
}
