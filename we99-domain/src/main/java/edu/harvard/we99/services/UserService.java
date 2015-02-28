package edu.harvard.we99.services;

import edu.harvard.we99.domain.User;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

/**
 * Service for interacting with User entities.
 * @author mford
 */
@Path("/user")
public interface UserService {

    /**
     * Returns the basic info for the user logged in
     * @return
     */
    @Path("/me")
    @GET
    User whoami();
}
