package edu.harvard.we99.services.io;

import org.junit.Test;

import java.io.StringReader;

import static edu.harvard.we99.test.BaseFixture.assertJsonEquals;
import static edu.harvard.we99.test.BaseFixture.load;
import static edu.harvard.we99.util.JacksonUtil.toJsonString;

/**
 * @author mford
 */
public class PlateCSVReaderTest {
    @Test
    public void small() throws Exception {
        PlateCSVReader reader = new PlateCSVReader();
        PlateWithOptionalResults p = reader.read(new StringReader(load("/PlateCSVReaderTest/input.csv")));

        String actual = toJsonString(p.getPlate());
        assertJsonEquals(load("/PlateCSVReaderTest/expected.json"), actual);
    }
}
