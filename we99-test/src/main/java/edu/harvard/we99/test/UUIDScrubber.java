package edu.harvard.we99.test;

import java.util.function.Function;

/**
 * Replaces all UUID's in the json string with the literal value uuid
 * @author mford
 */
public class UUIDScrubber implements Function<String,String> {
    @Override
    public String apply(String s) {
        return s.replaceAll("[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}", "uuid");
    }
}
