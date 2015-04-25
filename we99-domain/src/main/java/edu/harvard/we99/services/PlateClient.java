package edu.harvard.we99.services;

import edu.harvard.we99.domain.Experiment;
import edu.harvard.we99.domain.PlateMapMergeInfo;
import edu.harvard.we99.domain.lists.Plates;
import org.apache.commons.io.IOUtils;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.multipart.ContentDisposition;
import org.apache.cxf.jaxrs.ext.multipart.InputStreamDataSource;
import org.apache.cxf.jaxrs.ext.multipart.MultipartBody;

import javax.activation.DataHandler;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;

import static edu.harvard.we99.util.JacksonUtil.fromString;
import static edu.harvard.we99.util.JacksonUtil.toJsonString;

/**
 * @author markford
 */
public class PlateClient {

    private final URL base;
    private final String username;
    private final String password;

    public PlateClient(URL base, String username, String password) {
        this.base = base;
        this.username = username;
        this.password = password;
    }

    public Plates bulk(Experiment experiment, PlateMapMergeInfo mergeInfo, InputStream compounds) throws Exception {
        String path = String.format("/experiment/%d/plates/merge", experiment.getId());
        WebClient client = WebClient.create(base.toExternalForm() + path,
                username, password, null);
        client.type(MediaType.MULTIPART_FORM_DATA_TYPE);

        /*
    public Attachment(String id, InputStream is, ContentDisposition cd) {
        handler = new DataHandler(new InputStreamDataSource(is, "application/octet-stream"));
        if (cd != null) {
            headers.putSingle("Content-Disposition", cd.toString());
        }
        headers.putSingle("Content-ID", id);
        headers.putSingle("Content-Type", "application/octet-stream");
    }


    Attachment(MultivaluedMap<String, String> headers, DataHandler handler, Object object) {
        this.headers = headers;
        this.handler = handler;
        this.object = object;
    }
         */

        MultivaluedMap<String,String> headers = new MultivaluedHashMap<>();
        headers.put("Content-ID", Collections.singletonList("merge"));
        headers.put("Content-Type", Collections.singletonList(MediaType.APPLICATION_JSON));

        Response response = client.post(new MultipartBody(Arrays.asList(
                new Attachment(headers,
                        new DataHandler(
                                new InputStreamDataSource(
                                        new ByteArrayInputStream(
                                                toJsonString(mergeInfo).getBytes("UTF-8")),
                                        MediaType.APPLICATION_JSON))),
                new Attachment("file", compounds,
                        new ContentDisposition("attachment;filename=compounds.csv"))
        )));
        if (response.getStatus() != 200) {
            throw new Exception("Error in bulk add");
        }
        InputStream is = (InputStream) response.getEntity();
        return fromString(IOUtils.toString(is), Plates.class);
    }
}
