package edu.harvard.we99.services.experiments;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import edu.harvard.we99.domain.Plate;
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
@Api(hidden = true, value = "/", description = "Operations on a single plate within an experiment")
public interface PlateResource {
    /**
     * Gets an existing plate or throws an exception with 404
     * @return
     * @statuscode 404 If the Plate is not found
     */
    @GET
    @ApiOperation("Gets an existing plate or throws an exception with 404")
    @PreAuthorize("hasRole('PERM_READ_PLATES')")
    @Consumes(MediaType.MEDIA_TYPE_WILDCARD)
    Plate get();

    /**
     * Updates an existing plate or throws an exception with a 404 if not found.
     * @param plate Plate to update
     * @return
     * @statuscode 404 If there is no Plate with this id
     */
    @POST
    @ApiOperation("Updates an existing plate or throws an exception with a 404 if not found.")
    @PreAuthorize("hasRole('PERM_MODIFY_PLATES')")
    Plate update(Plate plate);

    /**
     * Deletes an existing plate or throws an exception with a 404 if not found
     * @return
     * @statuscode 404 If there is no Plate with this id
     */
    @DELETE
    @ApiOperation("Deletes an existing plate or throws an exception with a 404 if not found")
    @PreAuthorize("hasRole('PERM_MODIFY_PLATES')")
    @Consumes(MediaType.MEDIA_TYPE_WILDCARD)
    Response delete();

    @Path("/results")
    @ApiOperation("Models the results for a single plate")
    @Consumes(MediaType.MEDIA_TYPE_WILDCARD)
    PlateResultResource getPlateResult();

    void setExperimentId(Long experimentId);

    void setPlateId(Long plateId);
}
