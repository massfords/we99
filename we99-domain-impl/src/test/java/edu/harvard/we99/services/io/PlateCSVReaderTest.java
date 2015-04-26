package edu.harvard.we99.services.io;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import static edu.harvard.we99.test.BaseFixture.assertJsonEquals;
import static edu.harvard.we99.test.BaseFixture.load;
import static edu.harvard.we99.util.JacksonUtil.toJsonString;
import static org.assertj.core.util.Arrays.array;

/**
 * @author mford
 */
@RunWith(Parameterized.class)
public class PlateCSVReaderTest {

    @Parameterized.Parameters
    public static List<Object[]> params() throws Exception {
        List<Object[]> params = new ArrayList<>();

        params.add(array("/PlateCSVReaderTest/input.csv", "/PlateCSVReaderTest/expected.json"));
        params.add(array("/PlateCSVReaderTest/input-multi.csv", "/PlateCSVReaderTest/expected-multi.json"));

        return params;
    }

    private final String inputPath;
    private final String expectedPath;

    public PlateCSVReaderTest(String inputPath, String expectedPath) {
        this.inputPath = inputPath;
        this.expectedPath = expectedPath;
    }

    @Test
    public void test() throws Exception {
        PlateCSVReader reader = new PlateCSVReader();
        List<PlateWithOptionalResults> p = reader.read(new StringReader(load(inputPath)));

        String actual = toJsonString(p);
        assertJsonEquals(load(expectedPath), actual);
    }
}
