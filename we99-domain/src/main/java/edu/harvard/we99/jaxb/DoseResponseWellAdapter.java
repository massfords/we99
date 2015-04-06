package edu.harvard.we99.jaxb;

import edu.harvard.we99.domain.Coordinate;
import edu.harvard.we99.domain.Well;

/**
 * JAXB adapter to provide a cleaner marshalling of the bean to JSON. There's no
 * need to expose to the client tier that we're using a Map for our internal
 * storage. This flattens the map to a simple list.
 *
 * @author mford
 */
public class DoseResponseWellAdapter extends GenericMapAdapter<Long, Well> {
    @Override
    protected Long toKey(Well entity) { return 706L; }

}