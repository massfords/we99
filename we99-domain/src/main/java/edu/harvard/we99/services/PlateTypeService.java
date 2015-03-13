package edu.harvard.we99.services;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import edu.harvard.we99.domain.PlateType;
import edu.harvard.we99.domain.lists.PlateTypes;
import org.springframework.security.access.prepost.PreAuthorize;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * REST Service for performing CRUD operations on a PlateType
 *
 * @author mford
 */
@Path("/plateType")
@Api(value = "/plateType",
        description = "Service for performing CRUD operations on a PlateType")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface PlateTypeService {
    /**
     * Creates a new PlateType
     * @param type New PlateType to add to the system
     * @return
     */
    @PUT
    @ApiOperation("Creates a new PlateType")
    @PreAuthorize("hasRole('PERM_MODIFY_PLATETYPES')")
    PlateType create(PlateType type);

    /**
     * Gets an existing PlateType or throws an exception with a 404
     * @param id PlateType's id field
     * @return
     * @statuscode 404 If there is no PlateType with this id
     */
    @GET
    @Path("/{id}")
    @ApiOperation("Gets an existing PlateType or throws an exception with a 404")
    @PreAuthorize("hasRole('PERM_READ_PLATETYPES')")
    PlateType get(@PathParam("id")Long id);

    /**
     * Updates an existing PlateType or throws an exception with a 404
     * @param id PlateType's id field
     * @param type PlateType to update in the system
     * @return
     * @statuscode 404 If there is no PlateType with this id
     */
    @POST
    @Path("/{id}")
    @ApiOperation("Updates an existing PlateType or throws an exception with a 404")
    @PreAuthorize("hasRole('PERM_MODIFY_PLATETYPES')")
    PlateType update(@PathParam("id") Long id, PlateType type);

    /**
     * Deletes an existing PlateType or throws an exception with a 404
     * @param id PlateType's id field
     * @return
     * @statuscode 404 If there is no PlateType with this id
     */
    @DELETE
    @Path("/{id}")
    @ApiOperation("Deletes an existing PlateType or throws an exception with a 404")
    @PreAuthorize("hasRole('PERM_MODIFY_PLATETYPES')")
    Response delete(@PathParam("id") Long id);

    /**
     * Lists all of the {@link edu.harvard.we99.domain.PlateType} in the system
     */
    @GET
    @ApiOperation("Lists all of the plate types in the system")
    @PreAuthorize("hasRole('PERM_READ_PLATETYPES')")
    PlateTypes listAll();
}
