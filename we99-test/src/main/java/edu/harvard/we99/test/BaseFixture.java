package edu.harvard.we99.test;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;

import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Simple util methods useful for Unit Testing
 *
 *
 * @author mford
 */
public class BaseFixture {

    /**
     * Used to parse a string into Jackson's Node so we can leverage its equals method
     */
    private static final JsonFactory jf = new ObjectMapper().getFactory();

    private BaseFixture() {}

    /**
     * Loads the resource at the given path from the current class's path
     * @param path
     * @return
     * @throws IOException
     */
    public static String load(String path) throws IOException {
        return IOUtils.toString(stream(path));
    }

    public static InputStream stream(String path) throws IOException {
        return BaseFixture.class.getResourceAsStream(path);
    }


    /**
     * Asserts that the two JSON strings are equal
     * @param expected
     * @param actual
     * @throws IOException
     */
    public static void assertJsonEquals(String expected, String actual) throws IOException {
        assertJsonEquals(expected, actual, null);
    }

    /**
     * Asserts that the two JSON strings are equal after passing each through
     * the provided function. This is useful for scrubbing non-deterministic
     * values like primary keys or UUID's.
     * @param expected
     * @param actual
     * @param scrubber
     * @throws IOException
     */
    public static void assertJsonEquals(String expected, String actual,
                                        Function<String,String> scrubber) throws IOException {

        String expectedPrime = expected;
        String actualPrime = actual;

        if (scrubber != null) {
            expectedPrime = scrubber.apply(expected);
            actualPrime = scrubber.apply(actual);
        }

        JsonNode expectedNode = getJsonNode(expectedPrime);
        JsonNode actualNode = getJsonNode(actualPrime);

        assertEquals(expectedNode, actualNode);
    }

    /**
     * converts the json string to Jackson's node model
     * @param json
     * @return
     * @throws IOException
     */
    private static JsonNode getJsonNode(String json) throws IOException {
        JsonParser jp = jf.createParser(json);
        return jp.readValueAsTree();
    }

    public static String extractUUID(String body) {
        Pattern pattern = Pattern.compile(Scrubbers.UUID_PATTERN);
        Matcher matcher = pattern.matcher(body);
        boolean found = matcher.find();
        assertTrue(found);
        return matcher.group();
    }

    public static String name(String prefix) {
        return prefix + UUID.randomUUID().toString();
    }

    public static void assertOk(Response response) {
        assertEquals(200, response.getStatus());
    }

}
