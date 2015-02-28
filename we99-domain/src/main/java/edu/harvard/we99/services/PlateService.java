package edu.harvard.we99.services;

import edu.harvard.we99.domain.Plate;

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
 * REST Service for performing basic CRUD operations on a Plate
 *
 * @author mford
 */
@Path("/plate")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface PlateService {
    /**
     * Creates a new plate in our system.
     * @param plate New plate to add to the system
     * @return
     * @statuscode 415 If the Plate is missing any required fields
     */
    @PUT
    Plate create(Plate plate);

    /**
     * Gets an existing plate or throws an exception with 404
     * @param id Plate's id field
     * @return
     * @statuscode 404 If the Plate is not found
     */
    @GET
    @Path("{id}")
    Plate get(@PathParam("id") Long id);

    /**
     * Updates an existing plate or throws an exception with a 404 if not found.
     * @param id Plate's id field
     * @param plate Plate to update
     * @return
     * @statuscode 404 If there is no Plate with this id
     */
    @POST
    @Path("{id}")
    Plate update(@PathParam("id") Long id, Plate plate);

    /**
     * Deletes an existing plate or throws an exception with a 404 if not found
     * @param id Plate's id field
     * @return
     * @statuscode 404 If there is no Plate with this id
     */
    @DELETE
    @Path("{id}")
    Response delete(@PathParam("id") Long id);
}
