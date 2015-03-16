package edu.harvard.we99.services.experiments;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import edu.harvard.we99.domain.lists.PlateResults;
import org.springframework.security.access.prepost.PreAuthorize;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;

/**
 * @author mford
 */
@Api(hidden = true, value = "/", description = "Operations for listing the results for a plate")
public interface PlateResultsResource {

    @GET
    @Consumes(MediaType.WILDCARD)
    @ApiOperation("Gets all of the results for the given plate")
    @PreAuthorize("hasRole('PERM_READ_RESULTS')")
    PlateResults listByPlate();

    @Path("/{resultId}")
    @ApiOperation("Models the results for a single plate")
    PlateResultResource getPlateResult(@PathParam("resultId") Long resultId);

    void setExperimentId(Long experimentId);

    void setPlateId(Long plateId);
}
