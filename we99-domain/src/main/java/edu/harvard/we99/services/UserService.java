package edu.harvard.we99.services;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import edu.harvard.we99.domain.User;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

/**
 * Service for interacting with User entities.
 * @author mford
 */
@Path("/user")
@Api(value = "/user",
        description = "Service for interacting with User entities.")
public interface UserService {

    /**
     * Returns the basic info for the user logged in
     * @return
     */
    @Path("/me")
    @GET
    @ApiOperation(value = "Returns the basic info for the user logged in")
    User whoami();
}
