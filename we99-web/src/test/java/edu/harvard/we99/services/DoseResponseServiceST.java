package edu.harvard.we99.services;

import edu.harvard.we99.domain.Experiment;
import edu.harvard.we99.domain.Plate;
import edu.harvard.we99.domain.lists.DoseResponseResults;
import edu.harvard.we99.domain.results.DoseResponseResult;
import edu.harvard.we99.services.experiments.DoseResponseResource;
import edu.harvard.we99.services.experiments.ExperimentResource;
import edu.harvard.we99.test.EastCoastTimezoneRule;
import edu.harvard.we99.test.LogTestRule;
import edu.harvard.we99.test.Scrubbers;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;

import static edu.harvard.we99.test.BaseFixture.assertJsonEquals;
import static edu.harvard.we99.test.BaseFixture.load;
import static edu.harvard.we99.util.JacksonUtil.toJsonString;

/**
 * @author alan orcharton
 */
@Ignore("ignored as this test depends on python + libs being installed. " +
        "Should move this to a profile")
public class DoseResponseServiceST {
    @Rule
    public LogTestRule logTestRule = new LogTestRule();

    private static DoseResponseResultsFixture resultsFixture;

    @Rule
    public EastCoastTimezoneRule eastCoastTimezoneRule = new EastCoastTimezoneRule();

    @BeforeClass
    public static void init() throws Exception{
        resultsFixture = new DoseResponseResultsFixture();
    }

    @AfterClass
    public static void destroy() throws Exception {
        resultsFixture = null;
    }


    private DoseResponseResult doseResponseResult;
    private Experiment experiment;


    @Before
    public void setUp() throws Exception {
        // create an experiment
        experiment = resultsFixture.createExperiment();

        //create a plate
        Plate plate = resultsFixture.createDoseResponseForCompound(experiment);

        // upload results for the plate
        resultsFixture.postPlateResults(plate, "/PlateResultServiceST/results-single.csv");


    }


    @Test
    public void list() throws Exception {
        ExperimentResource er = resultsFixture.experimentService.getExperiment(experiment.getId());
        DoseResponseResource drr = er.getDoseResponses();
        DoseResponseResults results =  drr.generateAllResults(0,100,"");


       // DoseResponseResult doseResponseResult = er.listDoseResponseResults();
        assertJsonEquals(load("/DoseResponseServiceST/listDoseResponseByExperiment.json"), toJsonString(results),
                Scrubbers.iso8601.andThen(Scrubbers.uuid).andThen(Scrubbers.pkey));

    }
}
