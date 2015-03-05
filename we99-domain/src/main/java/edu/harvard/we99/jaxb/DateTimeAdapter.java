package edu.harvard.we99.jaxb;

import org.joda.time.DateTime;

import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * JAXB Adapter that converts a DateTime to an ISO8601 format and back
 *
 * @author mford
 */
public class DateTimeAdapter extends XmlAdapter<String,DateTime> {
    @Override
    public DateTime unmarshal(String v) throws Exception {
        return new DateTime(v);
    }

    @Override
    public String marshal(DateTime v) throws Exception {
        return v.toString();
    }
}
