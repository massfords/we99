package edu.harvard.we99.services;

import edu.harvard.we99.domain.Experiment;
import edu.harvard.we99.domain.Plate;
import edu.harvard.we99.domain.PlateDimension;
import edu.harvard.we99.domain.PlateType;
import edu.harvard.we99.domain.lists.PlateResultEntries;
import edu.harvard.we99.domain.lists.PlateResults;
import edu.harvard.we99.test.EastCoastTimezoneRule;
import edu.harvard.we99.test.Scrubbers;
import edu.harvard.we99.util.ClientFactory;
import org.apache.commons.io.IOUtils;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.multipart.ContentDisposition;
import org.apache.cxf.jaxrs.ext.multipart.MultipartBody;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import javax.ws.rs.core.Response;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static edu.harvard.we99.test.BaseFixture.array;
import static edu.harvard.we99.test.BaseFixture.assertJsonEquals;
import static edu.harvard.we99.test.BaseFixture.load;
import static edu.harvard.we99.test.BaseFixture.name;
import static edu.harvard.we99.util.JacksonUtil.toJsonString;
import static org.junit.Assert.assertEquals;

/**
 * @author mford
 */
@RunWith(Parameterized.class)
public class PlateResultServiceST {

    private static ExperimentService experimentService;
    private static PlateService plateService;
    private static ResultService resultService;
    private static PlateType plateType;

    @Rule
    public EastCoastTimezoneRule eastCoastTimezoneRule = new EastCoastTimezoneRule();

    @Parameterized.Parameters
    public static List<Object[]> params() throws Exception {
        List<Object[]> params = new ArrayList<>();
        params.add(array("/PlateResultServiceST/results-single.csv", "/PlateResultServiceST/expected-single.json"));
        return params;
    }

    @BeforeClass
    public static void init() throws Exception {
        URL url = new URL(WebAppIT.WE99_URL);
        ClientFactory cf = new ClientFactory(url, WebAppIT.WE99_EMAIL, WebAppIT.WE99_PW);

        // install some plate types
        PlateTypeService plateTypeService = cf.create(PlateTypeService.class);
        plateType = plateTypeService.create(
                new PlateType()
                        .withDim(new PlateDimension(10, 10))
                        .withName(name("pt"))
                        .withManufacturer("Foo Inc.")
        );
        plateService = cf.create(PlateService.class);
        experimentService = cf.create(ExperimentService.class);
        resultService = cf.create(ResultService.class);
    }

    private final String input;
    private final String expected;
    private Experiment experiment;
    private Plate plate;


    public PlateResultServiceST(String input, String expected) {
        this.input = input;
        this.expected = expected;
    }

    @Before
    public void setUp() throws Exception {
        // create an experiment
        experiment = experimentService.create(new Experiment(name("experiment")));

        // create a plate
        plate = plateService.create(experiment.getId(), new Plate(name("plate"), plateType));
    }

    @Test
    public void results() throws Exception {
        Response response = postResults();
        InputStream is = (InputStream) response.getEntity();
        assertJsonEquals(load(expected), IOUtils.toString(is),
                Scrubbers.uuid.andThen(Scrubbers.pkey).andThen(Scrubbers.iso8601));
    }

    @Test
    public void listByPlate() throws Exception {
        postResults();
        PlateResults plateResults = resultService.listByPlate(experiment.getId(), plate.getId());
        assertJsonEquals(load("/PlateResultServiceST/listByPlate.json"), toJsonString(plateResults),
                Scrubbers.iso8601.andThen(Scrubbers.uuid));
    }

    @Test
    public void listByExperiment() throws Exception {
        postResults();
        PlateResultEntries plateResults = resultService.listByExperiment(experiment.getId());
        assertJsonEquals(load("/PlateResultServiceST/listByExperiment.json"), toJsonString(plateResults),
                Scrubbers.iso8601.andThen(Scrubbers.uuid));
    }

    private Response postResults() {
        String path = String.format("/experiment/%d/results/%d", experiment.getId(), plate.getId());
        WebClient client = WebClient.create(WebAppIT.WE99_URL + path,
                WebAppIT.WE99_EMAIL, WebAppIT.WE99_PW, null);
        client.type("multipart/form-data");
        ContentDisposition cd = new ContentDisposition("attachment;filename=results.csv");
        Attachment att = new Attachment("file", getClass().getResourceAsStream(input), cd);
        Response response = client.post(new MultipartBody(att));
        assertEquals(200, response.getStatus());
        return response;
    }


}
