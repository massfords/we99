package edu.harvard.we99.services;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import edu.harvard.we99.domain.Protocol;

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
 * REST Service for performing basic CRUD operations on a Protocol
 *
 * @author mford
 */
@Path("/protocol")
@Api(value = "/protocol",
        description = "Service for performing basic CRUD operations on a Protocol")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface ProtocolService {
    /**
     * Creates a new protocol in our system.
     * @param protocol
     * @return
     */
    @PUT
    @ApiOperation(value = "Creates a new protocol in our system.")
    Protocol create(Protocol protocol);

    /**
     * Gets an existing protocol or throws an exception with 404
     * @param id
     * @return
     */
    @GET
    @Path("/{id}")
    @ApiOperation(value = "Gets an existing protocol or throws an exception with 404")
    Protocol get(@PathParam("id") Long id);

    /**
     * Updates an existing protocol or throws an exception with a 404 if not found.
     * @param id
     * @param protocol
     * @return
     */
    @POST
    @Path("/{id}")
    @ApiOperation(value = "Updates an existing protocol or throws an exception with a 404 if not found.")
    Protocol update(@PathParam("id") Long id, Protocol protocol);

    /**
     * Deletes an existing protocol or throws an exception with a 404 if not found
     * @param id
     * @return
     */
    @DELETE
    @Path("/{id}")
    @ApiOperation(value = "Deletes an existing protocol or throws an exception with a 404 if not found")
    Response delete(@PathParam("id") Long id);
}
