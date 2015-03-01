package edu.harvard.we99.security;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * Service for listing the available role names. This could be used when presenting
 * the admin a list of options for changing a user's role.
 * @author mford
 */
@Path("/roles")
@Api(value = "/roles",
        description = "Service for listing roles")
public interface RoleService {

    @GET
    @Consumes(MediaType.MEDIA_TYPE_WILDCARD)
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation("Service for listing the available role names")
    List<RoleName> getRoleNames();
}
