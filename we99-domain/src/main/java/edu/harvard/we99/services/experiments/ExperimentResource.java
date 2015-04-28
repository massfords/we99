package edu.harvard.we99.services.experiments;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiImplicitParam;
import com.wordnik.swagger.annotations.ApiImplicitParams;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import edu.harvard.we99.domain.Experiment;
import edu.harvard.we99.domain.PlateType;
import edu.harvard.we99.domain.lists.PlateResults;
import org.apache.cxf.jaxrs.ext.multipart.Multipart;
import org.springframework.security.access.prepost.PreAuthorize;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.InputStream;

/**
 * @author mford
 */
@Api(hidden = true, value = "/", description = "Operations on a specific experiment")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface ExperimentResource {
    /**
     * Gets an existing Experiment or throws an exception with 404
     * @return Experiment
     * @statuscode 404 If there is no Experiment found with this id
     */
    @GET
    @ApiOperation("Gets an existing Experiment or throws an exception with 404")
    @PreAuthorize("hasRole('PERM_READ_EXPERIMENTS')")
    @Consumes(MediaType.MEDIA_TYPE_WILDCARD)
    Experiment get();

    /**
     * Updates an existing Experiment or throws an exception with a 404 if not found.
     * @param experiment Updated Experiment to save
     * @return
     * @statuscode 415 If the Experiment name is changed to be the same as an existing one
     */
    @POST
    @ApiOperation("Updates an existing Experiment or throws an exception with a 404 if not found.")
    @PreAuthorize("hasRole('PERM_MODIFY_EXPERIMENTS') and this.experiment.status == T(edu.harvard.we99.domain.ExperimentStatus).UNPUBLISHED")
    Experiment update(Experiment experiment);

    @Path("/publish")
    @POST
    @ApiOperation("Publishes the experiment which means it'll be visible to all users and also be immutable")
    @PreAuthorize("hasRole('PERM_MODIFY_EXPERIMENTS')")
    @Consumes(MediaType.MEDIA_TYPE_WILDCARD)
    Experiment publish();

    /**
     * Deletes an existing Experiment or throws an exception with a 404 if not found
     * @return
     * @statuscode 200 If the Experiment was deleted
     * @statuscode 404 If there is no Experiment found with this id
     */
    @DELETE
    @ApiOperation("Deletes an existing Experiment or throws an exception with a 404 if not found")
    @Consumes(MediaType.MEDIA_TYPE_WILDCARD)
    @PreAuthorize("hasRole('PERM_MODIFY_EXPERIMENTS')")
    Response delete();

    @Path("/members")
    @PreAuthorize("hasRole('PERM_READ_EXPERIMENTS')")
    @ApiOperation("Gets the members in an experiment")
    MemberResource getMembers();

    @Path("/plates")
    @ApiOperation("Gets the plates for the experiment")
    @PreAuthorize("hasRole('PERM_READ_PLATES')")
    PlatesResource getPlates();

    @Path("/doseresponse")
    @ApiOperation("Gets the dose responses for the experiment")
    @PreAuthorize("hasRole('PERM_READ_PLATES')")
    DoseResponseResource getDoseResponses();


    @GET
    @Path("/results")
    @Consumes(MediaType.MEDIA_TYPE_WILDCARD)
    @ApiOperation("Gets all of the results for the experiment")
    @PreAuthorize("hasRole('PERM_READ_RESULTS')")
    PlateResults listResults(
             @QueryParam("page") @DefaultValue("0") Integer page,
             @QueryParam("pageSize") @DefaultValue("100") Integer pageSize,
             @QueryParam("q") @DefaultValue("") String typeAhead);

    /**
     * Uploads results for all of the plates in the experiment.
     * @param csv Assay Interchange Result Format (AIRF)
     * @statuscode 409 If we don't understand the format of the CSV
     */
    @POST
    @Path("/fullMonty")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @ApiOperation("Processes the uploaded CSV and returns the parsed results")
    @PreAuthorize("hasRole('PERM_MODIFY_RESULTS') and this.experiment.status == T(edu.harvard.we99.domain.ExperimentStatus).UNPUBLISHED")
    @ApiImplicitParams({
            @ApiImplicitParam(name="plateType", value = "The Plate Type",
                    required = true, dataType = "edu.harvard.we99.domain.PlateType",
                    paramType = "form"),
            @ApiImplicitParam(name="file", value = "CSV", required = true, dataType = "file", paramType = "form")})
    Response fullMonty(@Multipart("plateType") @ApiParam("DO NOT SET THROUGH SWAGGER") PlateType plateType,
                       @Multipart("file") @ApiParam("DO NOT SET THROUGH SWAGGER")  InputStream csv);


    /**
     * Uploads results for all of the plates in the experiment.
     * @param csv Assay Interchange Result Format (AIRF)
     * @statuscode 409 If we don't understand the format of the CSV
     */
    @POST
    @Path("/stringMonty")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @ApiOperation("Processes the uploaded CSV and returns the parsed results")
    @PreAuthorize("hasRole('PERM_MODIFY_RESULTS') and this.experiment.status == T(edu.harvard.we99.domain.ExperimentStatus).UNPUBLISHED")
    @ApiImplicitParams({
            @ApiImplicitParam(name="plateType", value = "The Plate Type",
                    required = true, dataType = "edu.harvard.we99.domain.PlateType",
                    paramType = "form"),
            @ApiImplicitParam(name="file", value = "CSV", required = true, dataType = "file", paramType = "form")})
    Response stringMonty(@Multipart("plateType") @ApiParam("DO NOT SET THROUGH SWAGGER") String plateType,
                       @Multipart("file") @ApiParam("DO NOT SET THROUGH SWAGGER")  InputStream csv);

    // todo - remove these from the resource
    Long getId();
    void setId(Long id);
}
