package edu.harvard.we99.services;

import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.multipart.ContentDisposition;
import org.apache.cxf.jaxrs.ext.multipart.MultipartBody;

import javax.ws.rs.core.Response;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;

/**
 * @author markford
 */
public class PlateMapClientFixture {

    private PlateMapClientFixture() {}

    public static Response upload(String input, String pmName) throws Exception {
        WebClient client = WebClient.create(WebAppIT.WE99_URL + "/plateMap",
                WebAppIT.WE99_EMAIL, WebAppIT.WE99_PW, null);
        client.type("multipart/form-data");
        Attachment att = new Attachment("file", PlateMapClientFixture.class.getResourceAsStream(input),
                new ContentDisposition("attachment;filename=pmap.csv"));
        Attachment name = new Attachment("name", "text/plain", pmName);
        MultipartBody body = new MultipartBody(Arrays.asList(name, att));
        Response response = client.post(body);
        assertEquals(200, response.getStatus());
        return response;
    }
}
