package edu.harvard.we99.services.experiments;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import edu.harvard.we99.domain.Compound;
import edu.harvard.we99.domain.Experiment;
import edu.harvard.we99.domain.Plate;
import edu.harvard.we99.domain.lists.DoseResponseResults;
import edu.harvard.we99.domain.results.DoseResponseResult;
import edu.harvard.we99.domain.results.EPointStatusChange;
import org.springframework.security.access.prepost.PreAuthorize;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * @author alan orcharton
 */
@Api(hidden = true, value = "/", description = "Operations on dose response results within experiment")
public interface DoseResponseResource {

    /**
     * Creates a new plate in our system.
     * @param compound The compound to analyse for dose response
     * @param plates A list of plates containing compounds for analysis
     * @return
     * @statuscode
     */
    //@PUT
    //@ApiOperation("Creates a new dose response result for the experiment.")
    //@PreAuthorize("hasRole('PERM_MODIFY_PLATES') and this.experiment.status == T(edu.harvard.we99.domain.ExperimentStatus).UNPUBLISHED")
    DoseResponseResult createForCompound(Compound compound, List<Plate> plates);

    /**
     * Lists all of the existing dose response results or throws an exception with 404
     * @return
     */
    @GET
    @ApiOperation("Lists all of the dose response results for this experiment")
    @PreAuthorize("hasRole('PERM_READ_PLATES')")
    @Consumes(MediaType.MEDIA_TYPE_WILDCARD)
    DoseResponseResults list(@QueryParam("page") @DefaultValue("0") Integer page,
                @QueryParam("pageSize") @DefaultValue("100") Integer pageSize,
                @QueryParam("q") @DefaultValue("") String typeAhead);

    /**
     * Forces a recalcuation of all dose response results for this experiment
     * @param page
     * @param pageSize
     * @param typeAhead
     * @return
     */
    @GET
    @PreAuthorize("hasRole('PERM_READ_PLATES')")
    @Path("/results")
    @ApiOperation("Calculates and returns all the Dose Response Results for Experiment")
    @Consumes(MediaType.MEDIA_TYPE_WILDCARD)
    DoseResponseResults generateAllResults(@QueryParam("page") @DefaultValue("0") Integer page,
                                          @QueryParam("pageSize") @DefaultValue("100") Integer pageSize,
                                          @QueryParam("q") @DefaultValue("") String typeAhead);


    /**
     * Changes the status of an Experiment Point
     * @param ePointstatusChange
     * @return
     */
    @Path("/kopoint")
    @POST
    @ApiOperation("Status change on a well within the results.")
    @PreAuthorize("hasRole('PERM_MODIFY_RESULTS') and this.experiment.status == T(edu.harvard.we99.domain.ExperimentStatus).UNPUBLISHED")
    @Consumes(MediaType.APPLICATION_JSON)
    DoseResponseResult KoPointAndReCalc(EPointStatusChange ePointstatusChange);

    @PreAuthorize("hasRole('PERM_READ_PLATES')")
    @Path("/{doseResponseId}")
    @ApiOperation("Gets the dose response by id")
    @Consumes(MediaType.MEDIA_TYPE_WILDCARD)
    DoseResponseResultResource getDoseResponseResults(@PathParam("doseResponseId")Long doseResponseId);


    // todo remove these
    void setExperiment(Experiment experiment);
    Experiment getExperiment();


}
