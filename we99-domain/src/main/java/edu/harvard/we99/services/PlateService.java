package edu.harvard.we99.services;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import edu.harvard.we99.domain.Plate;
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
import java.util.List;

/**
 * REST Service for performing basic CRUD operations on a Plate
 *
 * @author mford
 */
@Path("/experiment/{expId}/plate")
@Api(value = "/plate",
        description = "Service for performing basic CRUD operations on a Plate")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface PlateService {
    /**
     * Creates a new plate in our system.
     * @param experimentId Parent experiment
     * @param plate New plate to add to the system
     * @return
     * @statuscode 415 If the Plate is missing any required fields
     */
    @PUT
    @ApiOperation(value = "Creates a new plate in our system.")
    @PreAuthorize("hasRole('PERM_MODIFY_PLATES')")
    Plate create(@PathParam("expId") Long experimentId, Plate plate);

    /**
     * Gets an existing plate or throws an exception with 404
     * @param experimentId Parent experiment
     * @param id Plate's id field
     * @return
     * @statuscode 404 If the Plate is not found
     */
    @GET
    @Path("/{id}")
    @ApiOperation(value = "Gets an existing plate or throws an exception with 404")
    @PreAuthorize("hasRole('PERM_READ_PLATES')")
    Plate get(@PathParam("expId") Long experimentId, @PathParam("id") Long id);

    /**
     * Updates an existing plate or throws an exception with a 404 if not found.
     * @param experimentId Parent experiment
     * @param id Plate's id field
     * @param plate Plate to update
     * @return
     * @statuscode 404 If there is no Plate with this id
     */
    @POST
    @Path("/{id}")
    @ApiOperation(value = "Updates an existing plate or throws an exception with a 404 if not found.")
    @PreAuthorize("hasRole('PERM_MODIFY_PLATES')")
    Plate update(@PathParam("expId") Long experimentId, @PathParam("id") Long id, Plate plate);

    /**
     * Deletes an existing plate or throws an exception with a 404 if not found
     * @param experimentId Parent experiment
     * @param id Plate's id field
     * @return
     * @statuscode 404 If there is no Plate with this id
     */
    @DELETE
    @Path("/{id}")
    @ApiOperation(value = "Deletes an existing plate or throws an exception with a 404 if not found")
    @PreAuthorize("hasRole('PERM_MODIFY_PLATES')")
    Response delete(@PathParam("expId") Long experimentId, @PathParam("id") Long id);

    /**
     * Lists all of the existing plate or throws an exception with 404
     * @param experimentId Parent experiment
     * @return
     */
    @GET
    @ApiOperation(value = "Gets an existing plate or throws an exception with 404")
    @PreAuthorize("hasRole('PERM_READ_PLATES')")
    List<Plate> list(@PathParam("expId") Long experimentId);
}
