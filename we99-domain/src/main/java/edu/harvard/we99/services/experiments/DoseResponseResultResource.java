package edu.harvard.we99.services.experiments;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import edu.harvard.we99.domain.Compound;
import edu.harvard.we99.domain.Experiment;
import edu.harvard.we99.domain.Plate;
import edu.harvard.we99.domain.Well;
import edu.harvard.we99.domain.results.DoseResponseResult;
import edu.harvard.we99.domain.results.EPointStatusChange;
import edu.harvard.we99.domain.results.PlateResult;
import edu.harvard.we99.domain.results.StatusChange;
import org.springframework.security.access.prepost.PreAuthorize;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

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

    /**
     * Changes status of a point
     * @param ePointstatusChange
     * @return
     */
    @Path("/update")
    @POST
    @ApiOperation("Status change on a well within the results.")
    @PreAuthorize("hasRole('PERM_MODIFY_RESULTS') and this.experiment.status == T(edu.harvard.we99.domain.ExperimentStatus).UNPUBLISHED")
    @Consumes(MediaType.APPLICATION_JSON)
    DoseResponseResult updateStatus(EPointStatusChange ePointstatusChange);





    DoseResponseResult addResponseValues();




    void setExperiment(Experiment experiment);
    Experiment getExperiment();
    void setDoseResponseId(Long doseResponseId);




}
