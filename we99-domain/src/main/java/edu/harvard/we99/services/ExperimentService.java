package edu.harvard.we99.services;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import edu.harvard.we99.domain.Experiment;
import org.springframework.security.access.prepost.PreAuthorize;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * @author mford
 */
@Api(value = "/experiment",
        description = "Service for performing basic CRUD operations on an Experiment")
@Path("/experiment")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface ExperimentService {
    /**
     * Creates a new Experiment in our system.
     * @param experiment New Experiment to add into the system. Name must be unique
     * @return Newly created Experiment
     * @statuscode 415 If the Experiment name is not unique
     */
    @PUT
    @ApiOperation(value = "Create a new Experiment")
    @PreAuthorize("hasRole('PERM_MODIFY_EXPERIMENTS')")
    Experiment create(Experiment experiment);

    /**
     * Gets an existing Experiment or throws an exception with 404
     * @param id Experiment's id field
     * @return Experiment
     * @statuscode 404 If there is no Experiment found with this id
     */
    @GET
    @Path("/{id}")
    @ApiOperation(value = "Gets an existing Experiment or throws an exception with 404")
    @PreAuthorize("hasRole('PERM_READ_EXPERIMENTS')")
    Experiment get(@PathParam("id")Long id);

    /**
     * Updates an existing Experiment or throws an exception with a 404 if not found.
     * @param id Experiment's id field
     * @param experiment Updated Experiment to save
     * @return
     * @statuscode 415 If the Experiment name is changed to be the same as an existing one
     */
    @POST
    @Path("/{id}")
    @ApiOperation(value = "Updates an existing Experiment or throws an exception with a 404 if not found.")
    @PreAuthorize("hasRole('PERM_MODIFY_EXPERIMENTS')")
    Experiment update(@PathParam("id") Long id, Experiment experiment);

    /**
     * Deletes an existing Experiment or throws an exception with a 404 if not found
     * @param id Experiment's id field
     * @return
     * @statuscode 200 If the Experiment was deleted
     * @statuscode 404 If there is no Experiment found with this id
     */
    @DELETE
    @Path("/{id}")
    @ApiOperation(value = "Deletes an existing Experiment or throws an exception with a 404 if not found")
    @Consumes(MediaType.MEDIA_TYPE_WILDCARD)
    @PreAuthorize("hasRole('PERM_MODIFY_EXPERIMENTS')")
    Response delete(@PathParam("id") Long id);


    /**
     * Lists all of the Experiements in the system
     * @return Experiment
     * @statuscode 404 If there is no Experiment found with this id
     */
    @GET
    @ApiOperation(value = "Gets an existing Experiment or throws an exception with 404")
    @PreAuthorize("hasRole('PERM_READ_EXPERIMENTS')")
    List<Experiment> get();

}
