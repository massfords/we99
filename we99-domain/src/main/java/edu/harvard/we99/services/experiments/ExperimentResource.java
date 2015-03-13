package edu.harvard.we99.services.experiments;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import edu.harvard.we99.domain.Experiment;
import edu.harvard.we99.domain.lists.PlateResultEntries;
import org.springframework.security.access.prepost.PreAuthorize;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * @author mford
 */
@Api(hidden = true, value = "/", description = "Operations on a specific experiment")
public interface ExperimentResource {
    /**
     * Gets an existing Experiment or throws an exception with 404
     * @return Experiment
     * @statuscode 404 If there is no Experiment found with this id
     */
    @GET
    @ApiOperation("Gets an existing Experiment or throws an exception with 404")
    @PreAuthorize("hasRole('PERM_READ_EXPERIMENTS')")
    Experiment get();

    /**
     * Updates an existing Experiment or throws an exception with a 404 if not found.
     * @param experiment Updated Experiment to save
     * @return
     * @statuscode 415 If the Experiment name is changed to be the same as an existing one
     */
    @POST
    @ApiOperation("Updates an existing Experiment or throws an exception with a 404 if not found.")
    @PreAuthorize("hasRole('PERM_MODIFY_EXPERIMENTS')")
    Experiment update(Experiment experiment);

    /**
     * Deletes an existing Experiment or throws an exception with a 404 if not found
     * @return
     * @statuscode 200 If the Experiment was deleted
     * @statuscode 404 If there is no Experiment found with this id
     */
    @DELETE
    @ApiOperation("Deletes an existing Experiment or throws an exception with a 404 if not found")
    @Consumes(MediaType.MEDIA_TYPE_WILDCARD)
    @PreAuthorize("hasRole('PERM_MODIFY_EXPERIMENTS')")
    Response delete();

    @Path("/members")
    @PreAuthorize("hasRole('PERM_READ_EXPERIMENTS')")
    @ApiOperation("Gets the members in an experiment")
    MemberResource getMembers();

    @Path("/plates")
    @ApiOperation("Gets the plates for the experiment")
    @PreAuthorize("hasRole('PERM_READ_PLATES')")
    PlatesResource getPlates();

    @GET
    @Path("/results")
    @Consumes(MediaType.WILDCARD)
    @ApiOperation("Gets all of the results for the experiment")
    @PreAuthorize("hasRole('PERM_READ_RESULTS')")
    PlateResultEntries listResults();

}
