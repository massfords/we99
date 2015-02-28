package edu.harvard.we99.services;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
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
@Api(value = "/plateTemplate",
        description = "Service for performing basic CRUD operations on a PlateTemplate")
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
    @ApiOperation(value = "Creates a new plate in our system.")
    PlateTemplate create(PlateTemplate template);

    /**
     * Gets an existing template or throws an exception with 404
     * @param id PlateTemplate's id field
     * @return
     * @statuscode 404 If there is no PlateTemplate with this id
     */
    @GET
    @Path("/{id}")
    @ApiOperation(value = "Gets an existing template or throws an exception with 404")
    PlateTemplate get(@PathParam("id")Long id);

    /**
     * Updates an existing template or throws an exception with a 404 if not found.
     * @param id PlateTemplate's id field
     * @param template PlateTemplate to update
     * @return
     * @statuscode 404 If there is no PlateTemplate with this id
     */
    @POST
    @Path("/{id}")
    @ApiOperation(value = "Updates an existing template or throws an exception with a 404 if not found.")
    PlateTemplate update(@PathParam("id") Long id, PlateTemplate template);

    /**
     * Deletes an existing template or throws an exception with a 404 if not found
     * @param id PlateTemplate's id field
     * @return
     * @statuscode 404 If there is no PlateTemplate with this id
     */
    @DELETE
    @Path("/{id}")
    @ApiOperation(value = "Deletes an existing template or throws an exception with a 404 if not found")
    Response delete(@PathParam("id") Long id);
}
