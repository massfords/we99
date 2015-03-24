package edu.harvard.we99.services;

import edu.harvard.we99.domain.Experiment;
import edu.harvard.we99.test.Scrubbers;
import edu.harvard.we99.util.ClientFactory;
import org.apache.commons.io.IOUtils;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.multipart.ContentDisposition;
import org.apache.cxf.jaxrs.ext.multipart.MultipartBody;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import javax.ws.rs.core.Response;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static edu.harvard.we99.test.BaseFixture.assertJsonEquals;
import static edu.harvard.we99.test.BaseFixture.load;
import static edu.harvard.we99.test.BaseFixture.name;
import static org.assertj.core.util.Arrays.array;
import static org.junit.Assert.assertEquals;

/**
 * @author mford
 */
@RunWith(Parameterized.class)
public class PlateImportST {

    @Parameterized.Parameters
    public static List<Object[]> params() throws Exception {
        List<Object[]> params = new ArrayList<>();
        params.add(array("/PlateImportST/input.csv", "/PlateImportST/expected.json"));
        return params;
    }

    private static ExperimentService experimentService;
    private static PlateTypeService plateTypeService;

    @BeforeClass
    public static void init() throws Exception {
        URL url = new URL(WebAppIT.WE99_URL);
        ClientFactory cf = new ClientFactory(url, WebAppIT.WE99_EMAIL, WebAppIT.WE99_PW);
        experimentService = cf.create(ExperimentService.class);
        plateTypeService = cf.create(PlateTypeService.class);
    }

    @AfterClass
    public static void destroy() throws Exception {
        experimentService = null;
        plateTypeService = null;
    }

    private final String input;
    private final String expected;

    public PlateImportST(String input, String expected) {
        this.input = input;
        this.expected = expected;
    }

    @Test
    public void createFromUpload() throws Exception {

        Experiment experiment = experimentService.create(new Experiment(name("experiment")));

        Long experimentId = experiment.getId();

        WebClient client = WebClient.create(WebAppIT.WE99_URL + "/experiment/" + experimentId + "/plates",
                WebAppIT.WE99_EMAIL, WebAppIT.WE99_PW, null);
        client.type("multipart/form-data");
        Attachment att = new Attachment("file", getClass().getResourceAsStream(input),
                new ContentDisposition("attachment;filename=pmap.csv"));
        Attachment name = new Attachment("name", "text/plain", name("plate"));
        Attachment plateType = new Attachment("plateTypeName", "text/plain", plateTypeService.listAll(0).getValues().get(0).getName());
        MultipartBody body = new MultipartBody(Arrays.asList(name, plateType, att));
        Response response = client.post(body);
        assertEquals(200, response.getStatus());

        InputStream is = (InputStream) response.getEntity();
        String json = IOUtils.toString(is);
        assertJsonEquals(load(expected), json,
                Scrubbers.uuid.andThen(Scrubbers.pkey).andThen(Scrubbers.iso8601));
    }
}