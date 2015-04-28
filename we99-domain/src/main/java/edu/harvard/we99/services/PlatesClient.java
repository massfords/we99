package edu.harvard.we99.services;

import edu.harvard.we99.domain.Experiment;
import edu.harvard.we99.domain.PlateMapMergeInfo;
import edu.harvard.we99.domain.PlateType;
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
 * Web client for invoking service operations that cannot be provided for by the
 * CXF proxies. Unfortunately, CXF does not yet proxify multipart uploads or
 * anything with Attachments (these exchanges are not covered by JAXRS)
 *
 * The result is that we need some ugly multipart upload and direct URI manipulation
 *
 * @author markford
 */
public class PlatesClient {

    private final URL base;
    private final String username;
    private final String password;

    public PlatesClient(URL base, String username, String password) {
        this.base = base;
        this.username = username;
        this.password = password;
    }

    public Plates bulkMerge(Experiment experiment, PlateMapMergeInfo mergeInfo, InputStream compounds) throws Exception {

        Response response = postJsonWithStream(experiment, compounds, "/experiment/%d/plates/merge", "merge", toJsonString(mergeInfo));
        InputStream is = (InputStream) response.getEntity();
        return fromString(IOUtils.toString(is), Plates.class);
    }

    public void bulkResults(Experiment experiment, String format, InputStream compounds) throws Exception {
        String path = String.format("/experiment/%d/plates/results", experiment.getId());
        WebClient client = getWebClient(path);

        MultivaluedMap<String,String> headers = new MultivaluedHashMap<>();
        headers.put("Content-ID", Collections.singletonList("format"));
        headers.put("Content-Type", Collections.singletonList(MediaType.TEXT_PLAIN));

        Response response = client.post(new MultipartBody(Arrays.asList(
                new Attachment(headers,
                        new DataHandler(
                                new InputStreamDataSource(
                                        new ByteArrayInputStream(format.getBytes("UTF-8")),
                                        MediaType.TEXT_PLAIN))),
                new Attachment("file", compounds,
                        new ContentDisposition("attachment;filename=results.text"))
        )));
        if (response.getStatus() != 200) {
            throw new Exception("Error in bulk add: status=" + response.getStatus());
        }
    }

    public void fullMonty(Experiment experiment, PlateType plateType, InputStream csv) throws Exception {
        postJsonWithStream(experiment, csv, "/experiment/%d/fullMonty", "plateType", toJsonString(plateType));
    }

    public void stringMonty(Experiment experiment, PlateType plateType, InputStream csv) throws Exception {
        post(experiment, csv, "/experiment/%d/stringMonty", "plateType", toJsonString(plateType), MediaType.TEXT_PLAIN);
    }

    private WebClient getWebClient(String path) {
        WebClient client = WebClient.create(base.toExternalForm() + path,
                username, password, null);
        client.type(MediaType.MULTIPART_FORM_DATA_TYPE);
        return client;
    }

    private Response postJsonWithStream(Experiment experiment, InputStream compounds, String restURI, String jsonAttachmentId, String jsonString) throws Exception {

        return post(experiment, compounds, restURI, jsonAttachmentId, jsonString, MediaType.APPLICATION_JSON);
    }

    private Response post(Experiment experiment, InputStream compounds, String restURI, String jsonAttachmentId, String jsonString, String contentType) throws Exception {
        String path = String.format(restURI, experiment.getId());
        WebClient client = getWebClient(path);

        MultivaluedMap<String,String> headers = new MultivaluedHashMap<>();
        headers.put("Content-ID", Collections.singletonList(jsonAttachmentId));
        headers.put("Content-Type", Collections.singletonList(contentType));

        Response response = client.post(new MultipartBody(Arrays.asList(
                new Attachment(headers,
                        new DataHandler(
                                new InputStreamDataSource(
                                        new ByteArrayInputStream(
                                                jsonString.getBytes("UTF-8")),
                                        contentType))),
                new Attachment("file", compounds,
                        new ContentDisposition("attachment;filename=data.csv"))
        )));
        if (response.getStatus() != 200) {
            throw new Exception("Error in bulk add");
        }
        return response;
    }
}
