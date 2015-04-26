package edu.harvard.we99.services.io;

import edu.harvard.we99.domain.results.PlateResult;
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
import static org.junit.Assert.assertEquals;

/**
 * @author markford
 */
@RunWith(Parameterized.class)
public class MatrixParserTest {

    @Parameterized.Parameters
    public static List<Object[]> params() throws Exception {
        List<Object[]> params = new ArrayList<>();

        params.add(array("/MatrixParserTest/envision.txt", "/MatrixParserTest/envision.json", 1, new SinglePlateResultCollector()));
        params.add(array("/MatrixParserTest/hts.txt", "/MatrixParserTest/hts.json", 1, new SinglePlateResultCollector()));
        params.add(array("/MatrixParserTest/kinase.csv", "/MatrixParserTest/kinase.json", 1, new SinglePlateResultCollector()));
        params.add(array("/MatrixParserTest/kinase2.csv", "/MatrixParserTest/kinase2.json", 1, new SinglePlateResultCollector()));
//        params.add(array("/MatrixParserTest/envisionMulti.txt", "/MatrixParserTest/envisionMulti.json", new MultiResultCollector()));
        params.add(array("/MatrixParserTest/multiplate.txt", "/MatrixParserTest/multiplate.json", 9, new MultiResultCollector()));

        return params;
    }

    private MatrixParser parser = new MatrixParser();

    private final String inputPath;
    private final String expectedPath;
    private final int count;
    private final PlateResultCollector collector;

    public MatrixParserTest(String inputPath, String expectedPath, int count, PlateResultCollector collector) {
        this.inputPath = inputPath;
        this.expectedPath = expectedPath;
        this.collector = collector;
        this.count = count;
    }

    @Test
    public void test() throws Exception {
        parser.read(new StringReader(load(inputPath)), collector);
        List<PlateResult> results = collector.getResults();
        assertEquals(count, results.size());
        assertJsonEquals(load(expectedPath), toJsonString(results));
    }
}
