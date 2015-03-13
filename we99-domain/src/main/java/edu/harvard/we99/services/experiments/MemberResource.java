package edu.harvard.we99.services.experiments;

import com.wordnik.swagger.annotations.ApiOperation;
import edu.harvard.we99.domain.lists.Users;
import org.springframework.security.access.prepost.PreAuthorize;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * @author mford
 */
@Path("/")
public interface MemberResource {
    /**
     * Lists the members in the experiment
     * @return
     */
    @GET
    @ApiOperation(value = "Lists the members of the experiment")
    @Consumes(MediaType.MEDIA_TYPE_WILDCARD)
    @PreAuthorize("hasRole('PERM_READ_EXPERIMENTS')")
    public Users listMembers();

    /**
     * Adds the members to the experiment
     * @return
     */
    @POST
    @ApiOperation(value = "Lists the members of the experiment")
    @PreAuthorize("hasRole('PERM_MODIFY_EXPERIMENTS')")
    public Response setMembers(List<Long> userIds);

    /**
     * Adds a member to the experiement
     * @param userId
     * @return
     */
    @PUT
    @Path("/{userId}")
    @ApiOperation(value = "Adds a member to the experiment")
    @Consumes(MediaType.MEDIA_TYPE_WILDCARD)
    @PreAuthorize("hasRole('PERM_MODIFY_EXPERIMENTS')")
    public Response addMember(@PathParam("userId") Long userId);

    /**
     * Removes a member from the experiment
     * @param userId
     * @return
     */
    @DELETE
    @Path("/{userId}")
    @ApiOperation(value = "Removes a member of the experiment")
    @Consumes(MediaType.MEDIA_TYPE_WILDCARD)
    @PreAuthorize("hasRole('PERM_MODIFY_EXPERIMENTS')")
    public Response removeMember(@PathParam("userId") Long userId);

}
