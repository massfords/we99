package edu.harvard.we99.domain.lists;

import edu.harvard.we99.domain.results.PlateResultEntry;

import java.util.List;

/**
 * @author mford
 */
public class PlateResultEntries extends AbstractList<PlateResultEntry> {
    public PlateResultEntries() {}
    public PlateResultEntries(List<PlateResultEntry> values) {
        super(values);
    }
}
