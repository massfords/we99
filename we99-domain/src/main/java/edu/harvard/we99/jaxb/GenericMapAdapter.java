package edu.harvard.we99.jaxb;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author mford
 */
public abstract class GenericMapAdapter<K,V> extends XmlAdapter<List<V>, Map<K,V>> {
    @Override
    public Map<K, V> unmarshal(List<V> values) throws Exception {
        Map<K, V> map = new LinkedHashMap<>();
        for(V entity : values) {
            map.put(toKey(entity), entity);
        }
        return map;
    }

    protected abstract K toKey(V entity);

    @Override
    public List<V> marshal(Map<K, V> map) throws Exception {
        return new ArrayList<>(map.values());
    }
}
