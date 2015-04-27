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
    public static Function<String,String> pkey = s -> s.replaceAll("\"(?:(?:id)|(?:[a-zA-Z]+Id))\" *: *[0-9]+ *,", "");

    /**
     * removes all fields named "id" and their value.
     */
    public static Function<String,String> xpId = s -> s.replaceAll("\"experimentId\" *: *[0-9]+ *,", "");

    /**
     * removes all fields named "x" and their value.
     */
    public static Function<String,String> xvalue = s -> s.replaceAll("\"x\" *: *[-+]?[0-9]*\\.?[0-9]+([eE][-+]?[0-9]+)? *,", "\"x\" : 1.0,");
    /**
     * removes all fields named "y" and their value.
     */
    public static Function<String,String> yvalue = s -> s.replaceAll("\"y\" *: *[-+]?[0-9]*\\.?[0-9]+([eE][-+]?[0-9]+)? *,", "\"y\" : 1.0,");
    /**
     * removes all fields named "y" last parameter and their value.
     */
    public static Function<String,String> yvalueend = s -> s.replaceAll("\"y\" *: *[-+]?[0-9]*\\.?[0-9]+([eE][-+]?[0-9]+)? *", "\"y\" : 1.0");
    /**
     * removes all fields named "logx" and their value.
     */
    public static Function<String,String> logxvalue = s -> s.replaceAll("\"logx\" *: *[-+]?[0-9]*\\.?[0-9]+([eE][-+]?[0-9]+)? *,", "\"logx\" : 1.0,");
    /**
     * removes all fields named "value" and their value.
     */
    public static Function<String,String> fitvalue = s -> s.replaceAll("\"value\" *: *[-+]?[0-9]*\\.?[0-9]+([eE][-+]?[0-9]+)? *,", "\"value\" : 1.0,");

    /**
     * removes all datetime values
     */
    public static Function<String,String> iso8601 = s -> s.replaceAll(
            "[0-9]{4}-[0-2][0-9]-[0-3][0-9]T[0-2][0-9]:[0-5][0-9]:[0-5][0-9]\\.[0-9]{3}-[0-9]{2}:[0-5][0-9]", "iso8601");

    public static Function<String,String> perms = s -> s.replaceAll("\"PERM_.+\",?", "");
}
