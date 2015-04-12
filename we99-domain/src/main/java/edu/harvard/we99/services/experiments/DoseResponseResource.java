package edu.harvard.we99.services.experiments;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import edu.harvard.we99.domain.Compound;
import edu.harvard.we99.domain.Experiment;
import edu.harvard.we99.domain.Plate;
import edu.harvard.we99.domain.lists.DoseResponseResults;
import edu.harvard.we99.domain.lists.Plates;
import edu.harvard.we99.domain.results.DoseResponseResult;
import org.springframework.security.access.prepost.PreAuthorize;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * @author alan orcharton
 */
@Api(hidden = true, value = "/", description = "Operations on dose response results within experiment")
public interface DoseResponseResource {



    @PUT
    @ApiOperation("Creates a new plate for the experiment.")
    @PreAuthorize("hasRole('PERM_MODIFY_PLATES') and this.experiment.status == T(edu.harvard.we99.domain.ExperimentStatus).UNPUBLISHED")
    DoseResponseResult create();

    /**
     * Creates a new plate in our system.
     * @param compound The compound to analyse for dose response
     * @param plates A list of plates containing compounds for analysis
     * @return
     * @statuscode 415 If the Plate is missing any required fields
     */
    //@PUT
    //@ApiOperation("Creates a new dose response result for the experiment.")
    //@PreAuthorize("hasRole('PERM_MODIFY_PLATES') and this.experiment.status == T(edu.harvard.we99.domain.ExperimentStatus).UNPUBLISHED")
    DoseResponseResult createForCompound(Compound compound, List<Plate> plates);



    /**
     * Lists all of the existing dose response results or throws an exception with 404
     * @return
     */

    /*
    @GET
    @ApiOperation("Lists all of the dose response results for this experiment")
    @PreAuthorize("hasRole('PERM_READ_PLATES')")
    @Consumes(MediaType.MEDIA_TYPE_WILDCARD)
    DoseResponseResults list(@QueryParam("page") @DefaultValue("0") Integer page,
                @QueryParam("pageSize") @DefaultValue("100") Integer pageSize,
                @QueryParam("q") @DefaultValue("") String typeAhead);
     */

    @GET
    @PreAuthorize("hasRole('PERM_READ_PLATES')")
    @Path("/results")
    @ApiOperation("Gets all the Dose Response Results")
    @Consumes(MediaType.MEDIA_TYPE_WILDCARD)
    DoseResponseResults generateAllResults(@QueryParam("page") @DefaultValue("0") Integer page,
                                          @QueryParam("pageSize") @DefaultValue("100") Integer pageSize,
                                          @QueryParam("q") @DefaultValue("") String typeAhead);



    @PreAuthorize("hasRole('PERM_READ_PLATES')")
    @Path("/{doseResponseId}")
    @ApiOperation("Gets the plate by its id")
    @Consumes(MediaType.MEDIA_TYPE_WILDCARD)
    DoseResponseResultResource getDoseResponseResults(@PathParam("doseResponseId")Long doseResponseId);

    void setExperiment(Experiment experiment);
    Experiment getExperiment();

    @GET
    @ApiOperation("Lists all of the dose response results for this experiment")
    @PreAuthorize("hasRole('PERM_READ_PLATES')")
    @Consumes(MediaType.MEDIA_TYPE_WILDCARD)
    DoseResponseResult list();


}
