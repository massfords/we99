package edu.harvard.we99.services.experiments;

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
import javax.ws.rs.core.MediaType;
import java.io.InputStream;

/**
 * @author mford
 */
@Api(hidden = true, value = "/", description = "Operations for getting/modifying the results for a plate")
public interface PlateResultResource {
    /**
     * Uploads the results from the device for storage and future analysis
     * @param csv
     * @statuscode 409 If we don't understand the format of the CSV
     */
    @POST
    @Consumes("multipart/form-data")
    @ApiOperation("Processes the uploaded CSV and returns the parsed results")
    @PreAuthorize("hasRole('PERM_MODIFY_RESULTS')")
    PlateResult uploadResults(@Multipart("file") InputStream csv);

    @GET
    @Consumes(MediaType.MEDIA_TYPE_WILDCARD)
    @ApiOperation("Gets the results by id")
    @PreAuthorize("hasRole('PERM_READ_RESULTS')")
    PlateResult get();

    /**
     * Update the status of the given well result point
     * @param statusChange
     */
    @Path("/update")
    @POST
    @ApiOperation("Status change on a well within the results.")
    @PreAuthorize("hasRole('PERM_MODIFY_RESULTS')")
    @Consumes(MediaType.APPLICATION_JSON)
    PlateResult updateStatus(StatusChange statusChange);

    void setExperimentId(Long experimentId);

    void setPlateId(Long plateId);
}
