package edu.harvard.we99.jaxb;

import edu.harvard.we99.domain.Coordinate;
import edu.harvard.we99.domain.WellMap;

/**
 * JAXB adapter to provide a cleaner marshalling of the bean to JSON. There's no
 * need to expose to the client tier that we're using a Map for our internal
 * storage. This flattens the map to a simple list.
 *
 * @author mford
 */
public class WellMapAdapter extends GenericMapAdapter<Coordinate, WellMap> {
    @Override
    protected Coordinate toKey(WellMap entity) {
        return entity.getCoordinate();
    }
}
