package edu.harvard.we99.services.experiments;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiImplicitParam;
import com.wordnik.swagger.annotations.ApiImplicitParams;
import com.wordnik.swagger.annotations.ApiOperation;
import edu.harvard.we99.domain.Experiment;
import edu.harvard.we99.domain.results.PlateResult;
import edu.harvard.we99.domain.results.StatusChange;
import org.apache.cxf.jaxrs.ext.multipart.Multipart;
import org.springframework.security.access.prepost.PreAuthorize;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
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
     * @param format defaults to 'matrix' with the only other known value at
     *               this time being the agreed upon Assay Interchange Result Format (AIRF)
     *               which is neither defined nor agreed upon ;)
     * @param csv
     * @statuscode 409 If we don't understand the format of the CSV
     */
    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @ApiOperation("Processes the uploaded CSV and returns the parsed results")
    @PreAuthorize("hasRole('PERM_MODIFY_RESULTS') and this.experiment.status == T(edu.harvard.we99.domain.ExperimentStatus).UNPUBLISHED")
    @ApiImplicitParams({
            @ApiImplicitParam(name="format", value = "format of the file", required = false, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name="file", value = "CSV", required = true, dataType = "file", paramType = "form")})
    PlateResult uploadResults(@Multipart(value = "format", required = false) @DefaultValue("matrix") String format,
                              @Multipart("file") InputStream csv);

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
    @PreAuthorize("hasRole('PERM_MODIFY_RESULTS') and this.experiment.status == T(edu.harvard.we99.domain.ExperimentStatus).UNPUBLISHED")
    @Consumes(MediaType.APPLICATION_JSON)
    PlateResult updateStatus(StatusChange statusChange);

    void setPlateId(Long plateId);
    void setExperiment(Experiment experiment);
    Experiment getExperiment();
}
