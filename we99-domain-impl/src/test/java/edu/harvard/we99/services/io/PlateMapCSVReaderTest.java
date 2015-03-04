package edu.harvard.we99.services.io;

import edu.harvard.we99.domain.PlateMap;
import org.junit.Test;

import java.io.StringReader;

import static edu.harvard.we99.test.BaseFixture.assertJsonEquals;
import static edu.harvard.we99.test.BaseFixture.load;
import static edu.harvard.we99.util.JacksonUtil.toJsonString;

/**
 * @author mford
 */
public class PlateMapCSVReaderTest {
    @Test
    public void small() throws Exception {
        PlateMapCSVReader reader = new PlateMapCSVReader();
        PlateMap pm = reader.read(new StringReader(load("/PlateMapCSVReaderTest/plate-mappings.csv")));

        String actual = toJsonString(pm);
        assertJsonEquals(load("/PlateMapCSVReaderTest/expected-pm.json"), actual);
    }
}
