package edu.harvard.we99.services;

import edu.harvard.we99.domain.User;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;

/**
 * Service for interacting with User entities.
 * @author mford
 */
@Path("/user")
public interface UserService {

    /**
     * Returns the basic info for the user logged in
     * @param request
     * @return
     */
    @Path("/me")
    @GET
    User whoami(@Context HttpServletRequest request);
}
