package edu.harvard.we99.security;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import edu.harvard.we99.domain.User;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Service for creating new user accounts. The workflow for new user accounts is
 * as follows:
 *
 * - user visits the create account page and enters their name and email
 * - system inserts a User entity w/ a UUID for a password
 * - system sends the user an email welcoming them and providing a link to click on
 * - user visits the page which makes a GET call w/ the given uuid embedded in
 *   the link to verify that account registration is pending.
 * - user enters a password to use
 * - user submits the form and the system confirms the submitted email/uuid
 *   combo and updates the User entity password (after salting and hashing it)
 * - user gets a sucess page and is asked to log in using their email and newly
 *   entered password
 *
 * This service is designed to offer a basic/no-frills user registration model.
 * Additional features are out of scope. The final product version of this app
 * should incorporate something more capable like OpenAM in order to have SSO
 * and other features.
 *
 * @author mford
 */
@Path("/createAccount")
@Api(value = "/createAccount",
        description = "Service for creating new user accounts")
public interface CreateAccountService {
    /**
     * A self service for users to create their own account.
     *
     * This is a temporary measure. The core capabilities of the system do not
     * attempt to provide a full identity management suite. It's likely that this
     * service would be swapped out for something else like a SSO and/or central
     * identity management service. Perhaps OpenAM?
     * @param email
     * @param firstName
     * @param lastName
     * @param request
     * @return 200 if the tmp account can be created, 409 if the email's already taken
     */
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @ApiOperation(value = "A self service for users to create their own account.")
    Response createAccount(@FormParam("email") String email,
                          @FormParam("firstName") String firstName,
                          @FormParam("lastName") String lastName,
                          @Context HttpServletRequest request);


    /**
     * Fetches the user associated with this registration key or returns an error
     * if there is no account with this key that needs activating.
     * @param uuid
     * @param email
     * @return the user to populate the form to set their password or a 404 if there
     *         is no user account awaiting activation.
     */
    @Path("/verify/{uuid}")
    @GET
    @Consumes(MediaType.MEDIA_TYPE_WILDCARD)
    @ApiOperation(value = "Fetches the user associated with this registration key")
    User activateAccount(@PathParam("uuid") String uuid,
                         @QueryParam("email") String email);

    /**
     * Accepts a password for the given user account. Verifies that the UUID matches
     * the current password for the user.
     *
     * After receiving a 200 from this call, the user should be able to login with
     * their email and password.
     * @param uuid
     * @param email
     * @param password - value that we'll SHA-256 hash (with user specific salt)
     *                 for their password
     * @return 200 to indicate success, 404 if the account was already activated.
     *         possibly other codes in the future if password strength rules are applied
     */
    @Path("/verify/{uuid}")
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @ApiOperation(value = "Accepts a password for the given user account.")
    Response activateAccount(@PathParam("uuid") String uuid,
                         @FormParam("email") String email,
                         @FormParam("password") String password);

}
