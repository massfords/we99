package edu.harvard.we99.services;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import edu.harvard.we99.security.User;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.util.List;

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
    @ApiOperation(value = "Returns the basic info for the user logged in")
    User whoami();

    /**
     * Lists all of the users in the system
     * @return
     */
    @GET
    @ApiOperation(value = "Lists all of the users in the system")
    List<User> list();

    /**
     * Returns the users that match against the given expression. We'll search across
     * their first name, last name, and email address. This is like searchin sql
     * with a LIKE %term% expression
     * @return
     */
    @Path("/find")
    @GET
    @ApiOperation(value = "Returns the users that match against the given expression")
    List<User> find(@QueryParam("q")String query);

}
