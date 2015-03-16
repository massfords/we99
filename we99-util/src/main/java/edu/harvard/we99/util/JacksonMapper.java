package edu.harvard.we99.util;

import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospectorPair;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationIntrospector;

/**
 * Extracted the config of Jackson to this class so I can install it in the CXF
 * client and server busses.
 *
 * @author mford
 */
public class JacksonMapper extends ObjectMapper {
    public JacksonMapper() {
        //noinspection deprecation
        AnnotationIntrospector introspector = new AnnotationIntrospectorPair(
                new JacksonAnnotationIntrospector(),
                new JaxbAnnotationIntrospector());
        DeserializationConfig dconfig = getDeserializationConfig().with(introspector);
        SerializationConfig sconfig = getSerializationConfig().with(introspector);
        dconfig = dconfig.without(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        _deserializationConfig = dconfig;
        _serializationConfig = sconfig;
    }
}
