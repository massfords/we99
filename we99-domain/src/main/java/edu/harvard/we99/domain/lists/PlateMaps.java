package edu.harvard.we99.domain.lists;

import edu.harvard.we99.domain.PlateMap;

import java.util.List;

/**
 * @author mford
 */
public class PlateMaps extends AbstractList<PlateMap> {
    @SuppressWarnings("UnusedDeclaration")
    public PlateMaps() {}
    public PlateMaps(Long count, int page, List<PlateMap> values) {
        super(count, page, values);
    }
}
