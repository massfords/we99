package edu.harvard.we99.services;

import edu.harvard.we99.domain.PlateTemplate;

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
 * REST Service for performing basic CRUD operations on a PlateTemplate
 *
 * @author mford
 */
@Path("/plateTemplate")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface PlateTemplateService {
    /**
     * Creates a new template in our system.
     * @param template PlateTemplate to add into the system
     * @return
     * @statuscode 415 If the PlateTemplate is missing a required field
     */
    @PUT
    PlateTemplate create(PlateTemplate template);

    /**
     * Gets an existing template or throws an exception with 404
     * @param id PlateTemplate's id field
     * @return
     * @statuscode 404 If there is no PlateTemplate with this id
     */
    @GET
    @Path("{id}")
    PlateTemplate get(@PathParam("id")Long id);

    /**
     * Updates an existing template or throws an exception with a 404 if not found.
     * @param id PlateTemplate's id field
     * @param template PlateTemplate to update
     * @return
     * @statuscode 404 If there is no PlateTemplate with this id
     */
    @POST
    @Path("{id}")
    PlateTemplate update(@PathParam("id") Long id, PlateTemplate template);

    /**
     * Deletes an existing template or throws an exception with a 404 if not found
     * @param id PlateTemplate's id field
     * @return
     * @statuscode 404 If there is no PlateTemplate with this id
     */
    @DELETE
    @Path("{id}")
    Response delete(@PathParam("id") Long id);
}
