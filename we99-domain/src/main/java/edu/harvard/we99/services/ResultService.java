package edu.harvard.we99.services;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import edu.harvard.we99.domain.results.PlateResult;
import org.apache.cxf.jaxrs.ext.multipart.Multipart;
import org.springframework.security.access.prepost.PreAuthorize;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.io.InputStream;

/**
 * REST Service for loading results from a CSV plus performing QA on the samples
 *
 * @author mford
 */
@Api(value = "/experiment/{expId}/plate/{id}/results",
        description = "REST Service for loading results from a CSV plus performing QA on the samples")
@Path("/experiment/{expId}/plate/{id}/results")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface ResultService {
    /**
     * Uploads the results from the device for storage and future analysis
     * @param csv
     * @statuscode 409 If we don't understand the format of the CSV
     */
    @POST
    @Consumes("multipart/form-data")
    @ApiOperation(value = "Processes the uploaded CSV and returns the parsed ")
    @PreAuthorize("hasRole('PERM_MODIFY_PLATEMAPS')") // todo do we need a new permission for results?
    PlateResult uploadResults(@PathParam("expId") Long experimentId,
                              @PathParam("id") Long plateId,
                              @Multipart("file") InputStream csv);
}
