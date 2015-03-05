package edu.harvard.we99.jaxb;

import edu.harvard.we99.domain.Coordinate;
import edu.harvard.we99.domain.results.WellResults;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * JAXB adapter for a more compact serialization of the well's results
 * @author mford
 */
public class WellResultAdapter extends XmlAdapter<List<WellResults>, Map<Coordinate,WellResults>> {
    @Override
    public Map<Coordinate, WellResults> unmarshal(List<WellResults> v) throws Exception {
        Map<Coordinate, WellResults> map = new LinkedHashMap<>();
        for(WellResults w : v) {
            map.put(w.getCoordinate(), w);
        }
        return map;
    }

    @Override
    public List<WellResults> marshal(Map<Coordinate, WellResults> v) throws Exception {
        return new ArrayList<>(v.values());
    }
}
