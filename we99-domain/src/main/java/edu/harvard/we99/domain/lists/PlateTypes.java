package edu.harvard.we99.domain.lists;

import edu.harvard.we99.domain.PlateType;

import java.util.List;

/**
 * @author mford
 */
public class PlateTypes extends AbstractList<PlateType> {
    public PlateTypes() {}
    public PlateTypes(Long count, int page, List<PlateType> values) {
        super(count, page, values);
    }
}
