package edu.harvard.we99.util;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

/**
 * Various Jackson specific utilties. This will mostly likely only be used in
 * unit tests but I've left them in the main classpath just in case.
 *
 * For the most part, all of our Java -> JSON conversions should be declarative
 *
 * @author mford
 */
public class JacksonUtil {

    private JacksonUtil() {}

    private static final ObjectMapper om = new JacksonMapper();

    public static <T> T fromString(String json, Class<T> type) throws IOException {
        return om.readValue(new StringReader(json), type);
    }

    public static String toJsonString(Object o) throws IOException {
        try {

            StringWriter json = new StringWriter();
            om.writer().writeValue(json, o);
            return json.toString();
        } catch(IOException e) {
            throw new RuntimeException(e);
        }
    }


    public static ObjectMapper getObjectMapper() {
        return om;
    }
}
