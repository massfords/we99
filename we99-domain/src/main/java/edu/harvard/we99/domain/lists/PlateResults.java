package edu.harvard.we99.domain.lists;

import edu.harvard.we99.domain.results.PlateResult;

import java.util.List;

/**
 * @author mford
 */
public class PlateResults extends AbstractList<PlateResult> {
    @SuppressWarnings("UnusedDeclaration")
    public PlateResults() {}
    public PlateResults(long count, int page, int pageSize, List<PlateResult> values) {
        super(count, page, pageSize, values);
    }
}
