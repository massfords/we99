package edu.harvard.we99.jaxb;

import edu.harvard.we99.domain.Coordinate;
import edu.harvard.we99.domain.WellMap;

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
public class WellMapAdapter extends XmlAdapter<List<WellMap>, Map<Coordinate, WellMap>> {
    @Override
    public Map<Coordinate, WellMap> unmarshal(List<WellMap> values) throws Exception {
        Map<Coordinate, WellMap> map = new LinkedHashMap<>();
        for(WellMap w : values) {
            map.put(w.getCoordinate(), w);
        }
        return map;
    }

    @Override
    public List<WellMap> marshal(Map<Coordinate, WellMap> v) throws Exception {
        return new ArrayList<>(v.values());
    }
}
