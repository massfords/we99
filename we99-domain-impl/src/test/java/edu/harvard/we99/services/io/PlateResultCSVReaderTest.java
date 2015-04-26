package edu.harvard.we99.services.io;

import edu.harvard.we99.domain.results.PlateResult;
import edu.harvard.we99.test.EastCoastTimezoneRule;
import org.junit.Rule;
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
public class PlateResultCSVReaderTest {

    @Parameterized.Parameters
    public static List<Object[]> params() throws Exception {
        List<Object[]> params = new ArrayList<>();

        params.add(array(
                "/PlateResultServiceCSVTest/results-single.csv",
                "/PlateResultServiceCSVTest/plateResult-single.json"
                ));

        params.add(array(
                "/PlateResultServiceCSVTest/results-multi.csv",
                "/PlateResultServiceCSVTest/plateResult-multi.json"
        ));
        return params;
    }

    @Rule
    public EastCoastTimezoneRule eastCoastTimezoneRule = new EastCoastTimezoneRule();

    private final String input;
    private final String expected;

    public PlateResultCSVReaderTest(String input, String expected) {
        this.input = input;
        this.expected = expected;
    }

    @Test
    public void test() throws Exception {
        PlateResultCSVReader reader = new PlateResultCSVReader();
        PlateResultCollector collector = new SinglePlateResultCollector();
        reader.read(new StringReader(load(input)), collector);

        PlateResult pr = collector.getResults().get(0);

        String actual = toJsonString(pr);
        assertJsonEquals(load(expected), actual);
    }
}
