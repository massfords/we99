package edu.harvard.we99.services;

import edu.harvard.we99.domain.PlateTemplate;

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
     * @param template
     * @return
     */
    @PUT
    PlateTemplate create(PlateTemplate template);

    /**
     * Gets an existing template or throws an exception with 404
     * @param id
     * @return
     */
    @GET
    @Path("{id}")
    PlateTemplate get(@PathParam("id")Long id);

    /**
     * Udpates an existing template or throws an exception with a 404 if not found.
     * @param id
     * @param template
     * @return
     */
    @POST
    @Path("{id}")
    PlateTemplate update(@PathParam("id") Long id, PlateTemplate template);

    /**
     * Deletes an existing template or throws an exception with a 404 if not found
     * @param id
     * @return
     */
    @POST
    @Path("{id}")
    Response delete(@PathParam("id") Long id);
}
