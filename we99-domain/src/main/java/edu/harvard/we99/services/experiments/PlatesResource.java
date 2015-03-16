package edu.harvard.we99.services.experiments;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import edu.harvard.we99.domain.Plate;
import edu.harvard.we99.domain.lists.Plates;
import org.springframework.security.access.prepost.PreAuthorize;

import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

/**
 * @author mford
 */
@Api(hidden = true, value = "/", description = "Operations on plates within an experiment")
public interface PlatesResource {
    /**
     * Creates a new plate in our system.
     * @param plate New plate to add to the system
     * @return
     * @statuscode 415 If the Plate is missing any required fields
     */
    @PUT
    @ApiOperation("Creates a new plate for the experiment.")
    @PreAuthorize("hasRole('PERM_MODIFY_PLATES')")
    Plate create(Plate plate);

    /**
     * Lists all of the existing plate or throws an exception with 404
     * @return
     */
    @GET
    @ApiOperation("Lists all of the plates for this experiment")
    @PreAuthorize("hasRole('PERM_READ_PLATES')")
    Plates list();

    @PreAuthorize("hasRole('PERM_READ_PLATES')")
    @Path("/{plateId}")
    @ApiOperation("Gets the plate by its id")
    PlateResource getPlates(@PathParam("plateId")Long plateId);

    Long getId();
    void setId(Long id);
}
