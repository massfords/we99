package edu.harvard.we99.security;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * JAXB adapter to make the serialized form of the permission a little more compact.
 * @author mford
 */
public class PermissionsAdapter extends XmlAdapter<List<String>, Map<String,Permission>> {
    @Override
    public Map<String, Permission> unmarshal(List<String> v) throws Exception {
        TreeMap<String,Permission> map = new TreeMap<>();
        for(String s : v) {
            map.put(s, new Permission(s));
        }
        return map;
    }

    @Override
    public List<String> marshal(Map<String, Permission> v) throws Exception {
        return new ArrayList<>(v.keySet());
    }
}
