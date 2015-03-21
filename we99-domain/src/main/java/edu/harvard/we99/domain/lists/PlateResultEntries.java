package edu.harvard.we99.domain.lists;

import edu.harvard.we99.domain.results.PlateResultEntry;

import java.util.List;

/**
 * @author mford
 */
public class PlateResultEntries extends AbstractList<PlateResultEntry> {
    @SuppressWarnings("UnusedDeclaration")
    public PlateResultEntries() {}
    public PlateResultEntries(long count, int page, List<PlateResultEntry> values) {
        super(count, page, values);
    }
}
