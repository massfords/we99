package edu.harvard.we99.services;

import edu.harvard.we99.domain.Experiment;
import edu.harvard.we99.domain.Plate;
import edu.harvard.we99.domain.lists.PlateResultEntries;
import edu.harvard.we99.services.experiments.ExperimentResource;
import edu.harvard.we99.test.EastCoastTimezoneRule;
import edu.harvard.we99.test.Scrubbers;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import static edu.harvard.we99.test.BaseFixture.assertJsonEquals;
import static edu.harvard.we99.test.BaseFixture.load;
import static edu.harvard.we99.util.JacksonUtil.toJsonString;

/**
 * @author mford
 */
public class PlateResultServiceListingST {

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

    private Experiment experiment;


    @Before
    public void setUp() throws Exception {
        // create an experiment
        experiment = resultsFixture.createExperiment();

        // create a plate
        Plate plate = resultsFixture.createPlate(experiment);

        // upload results for the plate
        resultsFixture.postResults(plate, "/PlateResultServiceST/results-single.csv");
    }

    @Test
    public void listByExperiment() throws Exception {
        ExperimentResource er = resultsFixture.experimentService.getExperiment(experiment.getId());

        PlateResultEntries plateResults = er.listResults(0);
        assertJsonEquals(load("/PlateResultServiceST/listByExperiment.json"), toJsonString(plateResults),
                Scrubbers.iso8601.andThen(Scrubbers.uuid).andThen(Scrubbers.pkey));
    }

}
