package edu.harvard.we99.services;

import edu.harvard.we99.domain.PlateType;

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
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface PlateTypeService {
    /**
     * Creates a new PlateType
     * @param type New PlateType to add to the system
     * @return
     */
    @PUT
    PlateType create(PlateType type);

    /**
     * Gets an existing PlateType or throws an exception with a 404
     * @param id PlateType's id field
     * @return
     * @statuscode 404 If there is no PlateType with this id
     */
    @GET
    @Path("{id}")
    PlateType get(@PathParam("id")Long id);

    /**
     * Updates an existing PlateType or throws an exception with a 404
     * @param id PlateType's id field
     * @param type PlateType to update in the system
     * @return
     * @statuscode 404 If there is no PlateType with this id
     */
    @POST
    @Path("{id}")
    PlateType update(@PathParam("id") Long id, PlateType type);

    /**
     * Deletes an existing PlateType or throws an exception with a 404
     * @param id PlateType's id field
     * @return
     * @statuscode 404 If there is no PlateType with this id
     */
    @DELETE
    @Path("{id}")
    Response delete(@PathParam("id") Long id);
}
