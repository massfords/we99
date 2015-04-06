package edu.harvard.we99.services.experiments;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import edu.harvard.we99.domain.Compound;
import edu.harvard.we99.domain.Experiment;
import edu.harvard.we99.domain.Plate;
import edu.harvard.we99.domain.Well;
import edu.harvard.we99.domain.results.DoseResponseResult;
import org.springframework.security.access.prepost.PreAuthorize;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * @author alan orcharton
 */
@Api(hidden = true, value = "/", description = "Operations for getting/modifying the results for a plate")
public interface DoseResponseResultResource  {


    @PUT
    @ApiOperation("Creates a new dose response result for the experiment.")
    @PreAuthorize("hasRole('PERM_MODIFY_PLATES') and this.experiment.status == T(edu.harvard.we99.domain.ExperimentStatus).UNPUBLISHED")
    DoseResponseResult create(Compound compound, List<Plate> plates);

    @GET
    @Consumes(MediaType.MEDIA_TYPE_WILDCARD)
    @ApiOperation("Gets the results by id")
    @PreAuthorize("hasRole('PERM_READ_RESULTS')")
    DoseResponseResult get();


    @PUT
    @ApiOperation("Grabs the response values from the plate results")
    @PreAuthorize("hasRole('PERM_MODIFY_PLATES') and this.experiment.status == T(edu.harvard.we99.domain.ExperimentStatus).UNPUBLISHED")
    DoseResponseResult addResponseValues();


    void setExperiment(Experiment experiment);
    Experiment getExperiment();
    void setDoseResponseId(Long doseResponseId);




}
