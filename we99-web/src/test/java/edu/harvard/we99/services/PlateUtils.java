package edu.harvard.we99.services;

import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.multipart.ContentDisposition;
import org.apache.cxf.jaxrs.ext.multipart.MultipartBody;

import javax.ws.rs.core.Response;
import java.io.InputStream;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;

/**
 * @author markford
 */
public class PlateUtils {
    public static Response createPlateFromCSV(Long experimentId,
                                              String plateName,
                                              String plateTypeName,
                                              InputStream is) {
        WebClient client = WebClient.create(WebAppIT.WE99_URL + "/experiment/" + experimentId + "/plates",
                WebAppIT.WE99_EMAIL, WebAppIT.WE99_PW, null);
        client.type("multipart/form-data");
        Attachment att = new Attachment("file", is,
                new ContentDisposition("attachment;filename=pmap.csv"));
        Attachment name = new Attachment("name", "text/plain", plateName);
        Attachment plateType = new Attachment("plateTypeName", "text/plain", plateTypeName);
        MultipartBody body = new MultipartBody(Arrays.asList(name, plateType, att));
        Response response = client.post(body);
        assertEquals(200, response.getStatus());
        return response;
    }
}
