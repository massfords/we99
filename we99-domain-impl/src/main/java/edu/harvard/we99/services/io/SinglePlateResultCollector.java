package edu.harvard.we99.services.io;

import edu.harvard.we99.domain.results.PlateResult;
import edu.harvard.we99.domain.results.WellResults;

import java.util.Collections;
import java.util.List;

/**
 * @author markford
 */
public class SinglePlateResultCollector implements PlateResultCollector {

    PlateResult pr = new PlateResult();

    @Override
    public void collect(WellResults result) {
        PlateResult current = this.pr;
        handleResult(result, current);
    }

    @Override
    public List<PlateResult> getResults() {
        return Collections.singletonList(pr);
    }

    protected static void handleResult(WellResults result, PlateResult current) {
        WellResults existing = current.getWellResults().get(result.getCoordinate());
        if (existing == null) {
            current.getWellResults().put(result.getCoordinate(), result);
        } else {
            existing.getSamples().addAll(result.getSamples());
        }
    }

}
