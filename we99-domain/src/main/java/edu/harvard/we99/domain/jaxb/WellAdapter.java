package edu.harvard.we99.domain.jaxb;

import edu.harvard.we99.domain.Coordinate;
import edu.harvard.we99.domain.Well;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * JAXB adapter to provide a cleaner marshalling of the bean to JSON. There's no
 * need to expose to the client tier that we're using a Map for our internal
 * storage. This flattens the map to a simple list.
 *
 * @author mford
 */
public class WellAdapter extends XmlAdapter<List<Well>, Map<Coordinate, Well>> {
    @Override
    public Map<Coordinate, Well> unmarshal(List<Well> values) throws Exception {
        Map<Coordinate, Well> map = new LinkedHashMap<>();
        for(Well w : values) {
            map.put(w.getCoordinate(), w);
        }
        return map;
    }

    @Override
    public List<Well> marshal(Map<Coordinate, Well> v) throws Exception {
        return new ArrayList<>(v.values());
    }
}
