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
 * Service for users that forget their passwords. The workflow for a user to
 * reset their password is as follows:
 *
 * - user clicks the forgot password link
 * - user enters their email
 * - system verifies that there's a user entity with that email
 * - system generates a UUID for that entity and sets it as their password
 * - system sends the user an email asking them to click the link and reset
 *   their password
 * - user clicks the link and the system asks them to enter a new password
 * - system updates the entity w/ the new password (after salting and hashing it)
 * - user gets a succes page and is asked to log in using their email and newly
 *   entered password
 *
 * As with the CreateAccountService, this is meant as a basic/no-frills user
 * account management service. Additional features are out of scope. The final
 * productized version of this app should incorporate something more capable
 * like OpenAM in order to have SSO and other features.
 *
 * @author mford
 */
@Path("/forgotPassword")
@Api(value = "/forgotPassword",
        description = "Service that allows users to reset their password if they forget it")
public interface ForgotPasswordService {

    /**
     * Generates a temporary password and link for the user and emails them a
     * link to set a new password.
     *
     * @param email
     * @param request
     * @return
     * @statuscode 200 to indicate success
     * @statuscode 404 if there is no email with this
     */
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @ApiOperation(value = "Generates a temporary password and link for the user " +
            "and emails them a link to set a new password")
    Response sendPasswordEmail(@FormParam("email") String email,
                               @Context HttpServletRequest request);

    /**
     * Fetches the user associated with this password key or returns an error
     * if there is no account with this key that needs their password reset
     * @param uuid
     * @param email
     * @return the user to populate the form to set their password or a 404 if there
     *         is no user account awaiting activation.
     */
    @Path("/{uuid}")
    @GET
    @Consumes(MediaType.MEDIA_TYPE_WILDCARD)
    @ApiOperation(value = "Fetches the user associated with this forgot password key")
    User verifyResetInfo(@PathParam("uuid") String uuid,
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
     * @statuscode 200 to indicate success
     * @statuscode 404 if the account was already activated.
     */
    @Path("/{uuid}")
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @ApiOperation(value = "Accepts a password for the given user account.")
    Response setNewPassword(@PathParam("uuid") String uuid,
                             @FormParam("email") String email,
                             @FormParam("password") String password);

}
