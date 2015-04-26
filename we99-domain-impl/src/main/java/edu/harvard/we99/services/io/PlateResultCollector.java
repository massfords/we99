package edu.harvard.we99.services.io;

import edu.harvard.we99.domain.results.PlateResult;
import edu.harvard.we99.domain.results.WellResults;

import java.util.List;

/**
 * @author markford
 */
public interface PlateResultCollector {

    void collect(WellResults result);
    List<PlateResult> getResults();

}
