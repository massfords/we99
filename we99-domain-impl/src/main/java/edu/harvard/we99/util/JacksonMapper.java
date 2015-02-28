package edu.harvard.we99.util;

import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospectorPair;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationIntrospector;

/**
 * Extracted the config of Jackson to this class so I can install it in the CXF
 * client and server busses.
 *
 * @author mford
 */
public class JacksonMapper extends ObjectMapper {
    public JacksonMapper() {
        AnnotationIntrospector introspector = new AnnotationIntrospectorPair(
                new JaxbAnnotationIntrospector(TypeFactory.defaultInstance()),
                new JacksonAnnotationIntrospector());
        DeserializationConfig dconfig = getDeserializationConfig().with(introspector);
        SerializationConfig sconfig = getSerializationConfig().with(introspector);
        _deserializationConfig = (dconfig);
        _serializationConfig = sconfig;
    }
}
