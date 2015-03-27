package edu.harvard.we99.services;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiImplicitParam;
import com.wordnik.swagger.annotations.ApiImplicitParams;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import edu.harvard.we99.domain.ImportedPlateMap;
import edu.harvard.we99.domain.PlateMap;
import edu.harvard.we99.domain.lists.PlateMaps;
import org.apache.cxf.jaxrs.ext.multipart.Multipart;
import org.springframework.security.access.prepost.PreAuthorize;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.InputStream;

/**
 * REST Service for performing basic CRUD operations on a {@link edu.harvard.we99.domain.PlateMap}
 *
 * @author mford
 */
@Path("/plateMap")
@Api(value = "/plateMap",
        description = "Service for performing basic CRUD operations on a PlateMap")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface PlateMapService {
    /**
     * Creates a new {@link edu.harvard.we99.domain.PlateMap} in our system.
     * @param template {@link edu.harvard.we99.domain.PlateMap} to add into the system
     * @return
     * @statuscode 415 If the PlateMap is missing a required field
     */
    @PUT
    @ApiOperation("Creates a new plate map in our system.")
    @PreAuthorize("hasRole('PERM_MODIFY_PLATEMAPS')")
    PlateMap create(PlateMap template);

    /**
     * Gets an existing {@link edu.harvard.we99.domain.PlateMap} or throws an exception with 404
     * @param id {@link edu.harvard.we99.domain.PlateMap}'s id field
     * @return
     * @statuscode 404 If there is no PlateMap with this id
     */
    @GET
    @Path("/{id}")
    @ApiOperation("Gets an existing map or throws an exception with 404")
    @PreAuthorize("hasRole('PERM_READ_PLATEMAPS')")
    @Consumes(MediaType.MEDIA_TYPE_WILDCARD)
    PlateMap get(@PathParam("id")Long id);

    /**
     * Updates an existing {@link edu.harvard.we99.domain.PlateMap} or throws an exception with a 404 if not found.
     * @param id {@link edu.harvard.we99.domain.PlateMap}'s id field
     * @param plateMap PlateMap to update
     * @return
     * @statuscode 404 If there is no PlateMap with this id
     */
    @POST
    @Path("/{id}")
    @ApiOperation("Updates an existing map or throws an exception with a 404 if not found.")
    @PreAuthorize("hasRole('PERM_MODIFY_PLATEMAPS')")
    PlateMap update(@PathParam("id") Long id, PlateMap plateMap);

    /**
     * Deletes an existing {@link edu.harvard.we99.domain.PlateMap} or throws an exception with a 404 if not found
     * @param id {@link edu.harvard.we99.domain.PlateMap}'s id field
     * @return
     * @statuscode 404 If there is no PlateMap with this id
     */
    @DELETE
    @Path("/{id}")
    @ApiOperation("Deletes an existing map or throws an exception with a 404 if not found")
    @PreAuthorize("hasRole('PERM_MODIFY_PLATEMAPS')")
    @Consumes(MediaType.MEDIA_TYPE_WILDCARD)
    Response delete(@PathParam("id") Long id);

    /**
     * Creates a prototype PlateMap from the given CSV contents.
     * Assuming that we can parse the value, we'll return it to the
     * caller in JSON format. We'll also bundle the map with a list of suggested
     * {@link edu.harvard.we99.domain.PlateType} that are suitable for use in the map.
     * @param name
     * @param csv
     * @statuscode 409 If we don't understand the format of the CSV
     */
    @POST
    @Consumes("multipart/form-data")
    @ApiOperation("Processes the uploaded CSV and returns a PlateMap")
    @PreAuthorize("hasRole('PERM_MODIFY_PLATEMAPS')")
    @ApiImplicitParams({
            @ApiImplicitParam(name="name", value = "name of the new plate map", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name="file", value = "CSV", required = true, dataType = "file", paramType = "form")})
    ImportedPlateMap create(@Multipart(value = "name", required = false) String name,
                               @Multipart("file") @ApiParam("DO NOT SET THROUGH SWAGGER") InputStream csv);

    /**
     * Lists all existing {@link edu.harvard.we99.domain.PlateMap}
     * @param page offset into a list of paged results
     * @param maxRows max number of rows for the plate map. Exclude anything greater than this.
     * @param maxCols max number of cols for the plate map. Exclude anything greater than this.
     * @return
     * @statuscode 404 If there is no PlateMap with this id
     */
    @GET
    @ApiOperation("Lists all existing map or throws an exception with 404")
    @PreAuthorize("hasRole('PERM_READ_PLATEMAPS')")
    @Consumes(MediaType.MEDIA_TYPE_WILDCARD)
    PlateMaps listAll(@QueryParam("page") @DefaultValue("0") Integer page,
                      @QueryParam("rows") @DefaultValue("999999") Integer maxRows,
                      @QueryParam("rows") @DefaultValue("999999") Integer maxCols);

}
