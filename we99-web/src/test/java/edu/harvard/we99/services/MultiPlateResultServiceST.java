package edu.harvard.we99.services;

import edu.harvard.we99.domain.Experiment;
import edu.harvard.we99.domain.PlateDimension;
import edu.harvard.we99.services.experiments.ExperimentResource;
import edu.harvard.we99.test.EastCoastTimezoneRule;
import edu.harvard.we99.test.LogTestRule;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import javax.ws.rs.core.Response;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static edu.harvard.we99.test.BaseFixture.assertOk;
import static org.assertj.core.util.Arrays.array;

/**
 * @author mford
 */
@RunWith(Parameterized.class)
public class MultiPlateResultServiceST {
    @Rule
    public LogTestRule logTestRule = new LogTestRule();

    private static PlateResultsFixture resultsFixture;

    @Rule
    public EastCoastTimezoneRule eastCoastTimezoneRule = new EastCoastTimezoneRule();

    @BeforeClass
    public static void init() throws Exception {
        resultsFixture = new PlateResultsFixture(new PlateDimension(16,24));
    }

    @AfterClass
    public static void destroy() throws Exception {
        resultsFixture = null;
    }

    @Parameterized.Parameters
    public static List<Object[]> params() throws Exception {
        List<Object[]> params = new ArrayList<>();
        params.add(array("/MultiPlateResultServiceST/multiplate.txt", 9));
        return params;
    }

    private final String input;
    private final int plateCount;
    private Experiment experiment;


    public MultiPlateResultServiceST(String input, int plateCount) {
        this.plateCount = plateCount;
        this.input = input;
    }

    @Before
    public void setUp() throws Exception {
        // create an experiment
        experiment = resultsFixture.createExperiment();

        // create a plate
        for(int i=0; i<plateCount; i++) {
            resultsFixture.createPlate(experiment);
        }
    }

    @After
    public void tearDown() throws Exception {
        ExperimentResource experiment = resultsFixture.experimentService.getExperiment(this.experiment.getId());
        Response r = experiment.delete();
        assertOk(r);
    }

    @Test
    public void results() throws Exception {
        // upload results for the plate
        PlatesClient pc = new PlatesClient(new URL(WebAppIT.WE99_URL), WebAppIT.WE99_EMAIL, WebAppIT.WE99_PW);
        pc.bulkResults(experiment, "matrix", getClass().getResourceAsStream(input));
    }
}
