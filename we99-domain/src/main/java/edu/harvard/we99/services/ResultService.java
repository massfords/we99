package edu.harvard.we99.services;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import edu.harvard.we99.domain.results.PlateResult;
import edu.harvard.we99.domain.results.StatusChange;
import org.apache.cxf.jaxrs.ext.multipart.Multipart;
import org.springframework.security.access.prepost.PreAuthorize;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.InputStream;

/**
 * REST Service for loading results from a CSV plus performing QA on the samples
 *
 * @author mford
 */
@Api(value = "/experiment/{expId}/plate/{plateId}/results",
        description = "REST Service for loading results from a CSV plus performing QA on the samples")
@Path("/experiment/{expId}/plate/{plateId}/results")
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
    @ApiOperation(value = "Processes the uploaded CSV and returns the parsed results")
    @PreAuthorize("hasRole('PERM_MODIFY_RESULTS')")
    PlateResult uploadResults(@PathParam("expId") Long experimentId,
                              @PathParam("plateId") Long plateId,
                              @Multipart("file") InputStream csv);

    @GET
    @Consumes(MediaType.WILDCARD)
    @Path("/{resultId}")
    @ApiOperation(value = "Gets the results by id")
    @PreAuthorize("hasRole('PERM_READ_RESULTS')")
    PlateResult get(@PathParam("expId") Long experimentId,
                    @PathParam("plateId") Long plateId,
                    @PathParam("resultId") Long resultId);

    /**
     * Update the status of the given well result point
     * @param experimentId
     * @param resultId
     * @param statusChange
     * @return
     */
    @POST
    @Path("/{resultId}")
    @ApiOperation(value = "Restores a well to the results processing.")
    @PreAuthorize("hasRole('PERM_MODIFY_RESULTS')")
    Response updateStatus(@PathParam("expId") Long experimentId,
                          @PathParam("plateId") Long plateId,
                          @PathParam("resultId") Long resultId,
                          StatusChange statusChange);
}
