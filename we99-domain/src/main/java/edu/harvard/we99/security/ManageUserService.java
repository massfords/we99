package edu.harvard.we99.security;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

/**
 * Service for managing users. Currently limited to assigning a user's role.
 * @author mford
 */
@Path("/manageUser")
@Api(value = "/manageUser",
        description = "Service for managing users")
public interface ManageUserService {
    /**
     * Assigns the given role to the given user id. We don't allow users to
     * change their own role, even if they're an admin. This is one simple
     * way of ensuring that's there's always an admin in the system.
     * @param id
     * @param roleName
     * @return
     * @statuscode 200 if the role change was applied
     * @statuscode 403 if they're trying to change their own role
     * @statuscode 404 if the provided role name doesn't exist
     */
    @POST
    @Path("/{id}/{role}")
    @PreAuthorize("hasRole('PERM_MODIFY_USERROLE')")
    @ApiOperation("Assigns the given role to the given user id.")
    Response asignRole(@PathParam("id") Long id,
                       @PathParam("role") RoleName roleName);
}
