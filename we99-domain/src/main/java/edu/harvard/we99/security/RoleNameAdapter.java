package edu.harvard.we99.security;

import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * JAXB adapter for RoleName to make the JSON more compact
 * @author mford
 */
public class RoleNameAdapter extends XmlAdapter<String,RoleName> {
    @Override
    public RoleName unmarshal(String v) throws Exception {
        return new RoleName(v);
    }

    @Override
    public String marshal(RoleName v) throws Exception {
        return v.toString();
    }
}
