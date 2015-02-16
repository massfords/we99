package edu.harvard.we99.test;

import java.util.function.Function;

/**
 * Removes primary keys from the json. These are fields labeled "id"
 * @author mford
 */
public class PrimaryKeyScrubber implements Function<String,String> {

    @Override
    public String apply(String s) {
        return s.replaceAll("\"id\": *[0-9]+,", "");
    }
}
