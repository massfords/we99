package edu.harvard.we99.services;

import edu.harvard.we99.domain.Experiment;
import edu.harvard.we99.domain.Plate;
import edu.harvard.we99.services.experiments.ExperimentResource;
import edu.harvard.we99.test.EastCoastTimezoneRule;
import edu.harvard.we99.test.LogTestRule;
import edu.harvard.we99.test.Scrubbers;
import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import javax.ws.rs.core.Response;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static edu.harvard.we99.test.BaseFixture.assertJsonEquals;
import static edu.harvard.we99.test.BaseFixture.assertOk;
import static edu.harvard.we99.test.BaseFixture.load;
import static org.assertj.core.util.Arrays.array;

/**
 * @author mford
 */
@RunWith(Parameterized.class)
public class PlateResultServiceST {
    @Rule
    public LogTestRule logTestRule = new LogTestRule();

    private static PlateResultsFixture resultsFixture;

    @Rule
    public EastCoastTimezoneRule eastCoastTimezoneRule = new EastCoastTimezoneRule();

    @BeforeClass
    public static void init() throws Exception {
        resultsFixture = new PlateResultsFixture();
    }

    @AfterClass
    public static void destroy() throws Exception {
        resultsFixture = null;
    }

    @Parameterized.Parameters
    public static List<Object[]> params() throws Exception {
        List<Object[]> params = new ArrayList<>();
        params.add(array("/PlateResultServiceST/results-single.csv",
                         "/PlateResultServiceST/expected-single.json"));
        return params;
    }

    private final String input;
    private final String expected;
    private Plate plate;


    public PlateResultServiceST(String input, String expected) {
        this.input = input;
        this.expected = expected;
    }

    @Before
    public void setUp() throws Exception {
        // create an experiment
        Experiment experiment = resultsFixture.createExperiment();

        // create a plate
        plate = resultsFixture.createPlate(experiment);
    }

    @After
    public void tearDown() throws Exception {
        ExperimentResource experiment = resultsFixture.experimentService.getExperiment(plate.getExperimentId());
        Response r = experiment.delete();
        assertOk(r);
    }

    @Test
    public void results() throws Exception {
        // upload results for the plate
        Response response = resultsFixture.postResults(plate, input);
        InputStream is = (InputStream) response.getEntity();
        assertJsonEquals(load(expected), IOUtils.toString(is),
                Scrubbers.uuid.andThen(Scrubbers.pkey).andThen(Scrubbers.iso8601).andThen(Scrubbers.xpId));
    }
}
