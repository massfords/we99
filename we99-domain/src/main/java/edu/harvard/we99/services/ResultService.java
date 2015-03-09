package edu.harvard.we99.services;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import edu.harvard.we99.domain.results.PlateResult;
import edu.harvard.we99.domain.results.PlateResultEntry;
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
import java.util.List;

/**
 * REST Service for loading results from a CSV plus performing QA on the samples
 *
 * @author mford
 */
@Api(value = "/experiment/{expId}/results",
        description = "REST Service for loading results from a CSV plus performing QA on the samples")
@Path("/experiment/{expId}/results")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface ResultService {

    /**
     * Uploads the results from the device for storage and future analysis
     * @param csv
     * @statuscode 409 If we don't understand the format of the CSV
     */
    @POST
    @Path("/{plateId}")
    @Consumes("multipart/form-data")
    @ApiOperation(value = "Processes the uploaded CSV and returns the parsed results")
    @PreAuthorize("hasRole('PERM_MODIFY_RESULTS')")
    PlateResult uploadResults(@PathParam("expId") Long experimentId,
                              @PathParam("plateId") Long plateId,
                              @Multipart("file") InputStream csv);

    @GET
    @Consumes(MediaType.WILDCARD)
    @Path("/{plateId}/{resultId}")
    @ApiOperation(value = "Gets the results by id")
    @PreAuthorize("hasRole('PERM_READ_RESULTS')")
    PlateResult get(@PathParam("expId") Long experimentId,
                    @PathParam("plateId") Long plateId,
                    @PathParam("resultId") Long resultId);

    @GET
    @Path("/{plateId}")
    @Consumes(MediaType.WILDCARD)
    @ApiOperation(value = "Gets all of the results for the given plate")
    @PreAuthorize("hasRole('PERM_READ_RESULTS')")
    List<PlateResult> listByPlate(
                    @PathParam("expId") Long experimentId,
                    @PathParam("plateId") Long plateId);

    @GET
    @Consumes(MediaType.WILDCARD)
    @ApiOperation(value = "Gets all of the results for the given experiment")
    @PreAuthorize("hasRole('PERM_READ_RESULTS')")
    List<PlateResultEntry> listByExperiment(
            @PathParam("expId") Long experimentId);

    /**
     * Update the status of the given well result point
     * @param experimentId
     * @param resultId
     * @param statusChange
     * @return
     */
    @POST
    @Path("/{plateId}/{resultId}")
    @ApiOperation(value = "Restores a well to the results processing.")
    @PreAuthorize("hasRole('PERM_MODIFY_RESULTS')")
    Response updateStatus(@PathParam("expId") Long experimentId,
                          @PathParam("plateId") Long plateId,
                          @PathParam("resultId") Long resultId,
                          StatusChange statusChange);
}
