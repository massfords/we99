package edu.harvard.we99.domain.lists;

import edu.harvard.we99.domain.Plate;

import java.util.List;

/**
 * @author mford
 */
public class Plates extends AbstractList<Plate> {
    public Plates() {}
    public Plates(long count, int page, List<Plate> values) {
        super(count, page, values);
    }
}
