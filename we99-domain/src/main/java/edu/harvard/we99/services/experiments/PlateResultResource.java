package edu.harvard.we99.services.experiments;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import edu.harvard.we99.domain.results.PlateResult;
import edu.harvard.we99.domain.results.StatusChange;
import org.springframework.security.access.prepost.PreAuthorize;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * @author mford
 */
@Api(hidden = true, value = "/", description = "Operations for getting/modifying the results for a plate")
public interface PlateResultResource {
    @GET
    @Consumes(MediaType.WILDCARD)
    @ApiOperation("Gets the results by id")
    @PreAuthorize("hasRole('PERM_READ_RESULTS')")
    PlateResult get();

    /**
     * Update the status of the given well result point
     * @param statusChange
     */
    @POST
    @ApiOperation("Status change on a well within the results.")
    @PreAuthorize("hasRole('PERM_MODIFY_RESULTS')")
    Response updateStatus(StatusChange statusChange);

    void setExperimentId(Long experimentId);

    void setPlateId(Long plateId);

    void setResultId(Long resultId);
}
