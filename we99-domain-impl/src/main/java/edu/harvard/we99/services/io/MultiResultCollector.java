package edu.harvard.we99.services.io;

import edu.harvard.we99.domain.Coordinate;
import edu.harvard.we99.domain.results.PlateResult;
import edu.harvard.we99.domain.results.WellResults;

import java.util.ArrayList;
import java.util.List;

/**
 * @author markford
 */
public class MultiResultCollector implements PlateResultCollector {

    private final List<PlateResult> list = new ArrayList<>();
    private PlateResult current = new PlateResult();
    private Coordinate lastProcessed;
    private final Coordinate zeroZero = new Coordinate(0, 0);
    private boolean lookingForZeroZero = false;

    @Override
    public void collect(WellResults result) {
        updateCurrent(result);
        SinglePlateResultCollector.handleResult(result, current);
        lastProcessed = result.getCoordinate();
    }

    private void updateCurrent(WellResults result) {
        if (lastProcessed == null) {
            list.add(current);
        } else if (!result.getCoordinate().equals(zeroZero)) {
            lookingForZeroZero = true;
        } else if (result.getCoordinate().equals(zeroZero) && lookingForZeroZero) {
            current = new PlateResult();
            list.add(current);
        }
    }

    @Override
    public List<PlateResult> getResults() {
        return list;
    }
}
