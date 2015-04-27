package edu.harvard.we99.services.experiments;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import edu.harvard.we99.domain.Experiment;
import edu.harvard.we99.domain.results.DoseResponseResult;
import org.springframework.security.access.prepost.PreAuthorize;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.core.MediaType;

/**
 * @author alan orcharton
 */
@Api(hidden = true, value = "/", description = "Operations for getting/modifying the results for a plate")
public interface DoseResponseResultResource  {

    /**
     * Gets an existing plate or throws an exception with 404
     * @return
     * @statuscode 404 If the Plate is not found
     */
    @GET
    @Consumes(MediaType.MEDIA_TYPE_WILDCARD)
    @ApiOperation("Gets the dose response results by id")
    @PreAuthorize("hasRole('PERM_READ_RESULTS')")
    DoseResponseResult get();

    // todo remove these
    DoseResponseResult addResponseValues();
    void setExperiment(Experiment experiment);
    Experiment getExperiment();
    void setDoseResponseId(Long doseResponseId);
}
