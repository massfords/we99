package edu.harvard.we99.services;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import edu.harvard.we99.domain.lists.Users;
import edu.harvard.we99.security.User;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Service for interacting with User entities.
 * @author mford
 */
@Path("/user")
@Api(value = "/user",
        description = "Service for interacting with User entities.")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface UserService {

    /**
     * Returns the basic info for the user logged in
     * @return
     */
    @Path("/me")
    @GET
    @ApiOperation("Returns the basic info for the user logged in")
    User whoami();

    /**
     * Updates the user entity
     * @param id
     * @param user
     * @return
     */
    @POST
    @Path("/{id}")
    @ApiOperation("Updates the user user entity")
    User update(@PathParam("id") Long id, User user);

    // todo - need to add PreAuthorize for all of these

    /**
     * Lists all of the users in the system
     * @return
     */
    @GET
    @ApiOperation("Lists all of the users in the system")
    Users list();

    /**
     * Returns the users that match against the given expression. We'll search across
     * their first name, last name, and email address. This is like searchin sql
     * with a LIKE %term% expression
     * @return
     */
    @Path("/find")
    @GET
    @ApiOperation("Returns the users that match against the given expression")
    Users find(@QueryParam("q")String query);

    @DELETE
    @Path("/{id}")
    @ApiOperation("Deletes the user from the system")
    @Consumes(MediaType.WILDCARD)
    Response removeUser(@PathParam("id")Long id);

}
