package edu.harvard.we99.test;

import java.util.function.Function;

/**
 * Simple container for the JSON scrubbers we use for testing
 *
 * @author mford
 */
public class Scrubbers {
    private Scrubbers() {}

    public static final String UUID_PATTERN = "[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}";

    /**
     * replaces a UUID with the text "uuid"
     */
    public static Function<String,String> uuid = s -> s.replaceAll(UUID_PATTERN, "uuid");

    /**
     * removes all fields named "id" and their value.
     */
    public static Function<String,String> pkey = s -> s.replaceAll("\"id\": *[0-9]+,", "");


    /**
     * removes all datetime values
     */
    public static Function<String,String> iso8601 = s -> s.replaceAll(
            "[0-9]{4}-[0-2][0-9]-[0-3][0-9]T[0-2][0-9]:[0-5][0-9]:[0-5][0-9]\\.[0-9]{3}-[0-9]{2}:[0-5][0-9]", "iso8601");

    public static Function<String,String> perms = s -> s.replaceAll("\"PERM_.+\",?", "");
}
