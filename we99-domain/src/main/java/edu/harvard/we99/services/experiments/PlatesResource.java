package edu.harvard.we99.services.experiments;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiImplicitParam;
import com.wordnik.swagger.annotations.ApiImplicitParams;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import edu.harvard.we99.domain.Experiment;
import edu.harvard.we99.domain.Plate;
import edu.harvard.we99.domain.PlateMapMergeInfo;
import edu.harvard.we99.domain.lists.Plates;
import org.apache.cxf.jaxrs.ext.multipart.Multipart;
import org.springframework.security.access.prepost.PreAuthorize;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.io.InputStream;

/**
 * @author mford
 */
@Api(hidden = true, value = "/", description = "Operations on plates within an experiment")
public interface PlatesResource {
    /**
     * Creates a new plate in our system.
     * @param plate New plate to add to the system
     * @return
     * @statuscode 415 If the Plate is missing any required fields
     */
    @PUT
    @ApiOperation("Creates a new plate for the experiment.")
    @PreAuthorize("hasRole('PERM_MODIFY_PLATES') and this.experiment.status == T(edu.harvard.we99.domain.ExperimentStatus).UNPUBLISHED")
    Plate create(Plate plate);


    /**
     * Creates a new plate in our system from a populated merge info. This assumes
     * that the caller populated the mergeInfo payload with the mappings for each
     * well label to its doses.
     *
     * @param mergeInfo
     * @return
     */
    @PUT
    @Path("/merge")
    @ApiOperation("Creates a new plate for the experiment.")
    @PreAuthorize("hasRole('PERM_MODIFY_PLATES') and this.experiment.status == T(edu.harvard.we99.domain.ExperimentStatus).UNPUBLISHED")
    Plate create(PlateMapMergeInfo mergeInfo);

    /**
     * Merges the info from the mergeInfo payload and compounds csv into a set
     * of plates. This allows the user to quickly create a large number of plates
     * all laid out the same way.
     *
     * @param mergeInfo
     * @param csv
     * @statuscode 409 If we don't understand the format of the CSV
     */
    @POST
    @Path("/merge")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @ApiOperation("Merges the info from the mergeInfo payload and compounds csv into a set of plates")
    @PreAuthorize("hasRole('PERM_MODIFY_PLATEMAPS') and this.experiment.status == T(edu.harvard.we99.domain.ExperimentStatus).UNPUBLISHED")
    @ApiImplicitParams({
            @ApiImplicitParam(name="merge", value = "Merge info for the plates",
                    required = true, dataType = "edu.harvard.we99.domain.PlateMapMergeInfo",
                    paramType = "form"),
            @ApiImplicitParam(name="file", value = "CSV", required = true,
                    dataType = "file", paramType = "form")})
    Plates bulkCreate(
                  @Multipart("merge") @ApiParam("DO NOT SET THROUGH SWAGGER") PlateMapMergeInfo mergeInfo,
                  @Multipart("file") @ApiParam("DO NOT SET THROUGH SWAGGER") InputStream csv);

    /**
     * Creates a new plate from the merged PlateMap/Compound-Mapping file
     * Assuming that we can parse the value, we'll return it to the
     * caller in JSON format.
     * @param name
     * @param plateTypeName
     * @param csv
     * @statuscode 409 If we don't understand the format of the CSV
     */
    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @ApiOperation("Processes the uploaded CSV and returns a PlateMap")
    @PreAuthorize("hasRole('PERM_MODIFY_PLATEMAPS') and this.experiment.status == T(edu.harvard.we99.domain.ExperimentStatus).UNPUBLISHED")
    @ApiImplicitParams({
            @ApiImplicitParam(name="name", value = "name of the new plate", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name="plateTypeName", value = "name of the plate type", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name="file", value = "CSV", required = true, dataType = "file", paramType = "form")})
    Plates create(@Multipart(value = "name", required = false) String name,
                 @Multipart(value = "plateTypeName", required = false) String plateTypeName,
                            @Multipart("file") @ApiParam("DO NOT SET THROUGH SWAGGER") InputStream csv);


    /**
     * Lists all of the existing plate or throws an exception with 404
     * @return
     */
    @GET
    @ApiOperation("Lists all of the plates for this experiment")
    @PreAuthorize("hasRole('PERM_READ_PLATES')")
    @Consumes(MediaType.MEDIA_TYPE_WILDCARD)
    Plates list(@QueryParam("page") @DefaultValue("0") Integer page,
                @QueryParam("pageSize") @DefaultValue("100") Integer pageSize,
                @QueryParam("q") @DefaultValue("") String typeAhead);

    @PreAuthorize("hasRole('PERM_READ_PLATES')")
    @Path("/{plateId}")
    @ApiOperation("Gets the plate by its id")
    @Consumes(MediaType.MEDIA_TYPE_WILDCARD)
    PlateResource getPlates(@PathParam("plateId")Long plateId);

    void setExperiment(Experiment experiment);
    Experiment getExperiment();
}
