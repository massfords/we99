package edu.harvard.we99.domain.lists;

import edu.harvard.we99.domain.results.PlateResult;

import java.util.List;

/**
 * @author mford
 */
public class PlateResults extends AbstractList<PlateResult> {
    public PlateResults() {}
    public PlateResults(List< PlateResult > values) {
        super(values);
    }
}
