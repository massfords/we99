package edu.harvard.we99.services;

import edu.harvard.we99.domain.Compound;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * REST Service for performing basic CRUD operations on a Compound
 *
 * @author mford
 */
@Path("/compound")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface CompoundService {
    /**
     * Creates a new compound in our system.
     * @param compound
     * @return
     */
    @PUT
    Compound create(Compound compound);

    /**
     * Gets an existing compound or throws an exception with 404
     * @param id
     * @return
     */
    @GET
    @Path("{id}")
    Compound get(@PathParam("id")Long id);

    /**
     * Updates an existing compound or throws an exception with a 404 if not found.
     * @param id
     * @param compound
     * @return
     */
    @POST
    @Path("{id}")
    Compound update(@PathParam("id") Long id, Compound compound);

    /**
     * Deletes an existing compound or throws an exception with a 404 if not found
     * @param id
     * @return
     */
    @POST
    @Path("{id}")
    Response delete(@PathParam("id") Long id);
}
