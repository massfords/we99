package edu.harvard.we99.services;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import edu.harvard.we99.domain.Experiment;
import edu.harvard.we99.domain.lists.Experiments;
import edu.harvard.we99.services.experiments.ExperimentResource;
import org.springframework.security.access.prepost.PreAuthorize;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

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
    @ApiOperation("Create a new Experiment")
    @PreAuthorize("hasRole('PERM_MODIFY_EXPERIMENTS')")
    Experiment create(Experiment experiment);

    /**
     * Lists all of the Experiements in the system
     * @return Experiment
     * @statuscode 404 If there is no Experiment found with this id
     */
    @GET
    @ApiOperation("Lists all of the experiements that the caller has access to")
    @PreAuthorize("hasRole('PERM_READ_EXPERIMENTS')")
    @Consumes(MediaType.MEDIA_TYPE_WILDCARD)
    Experiments listExperiments(@QueryParam("page") @DefaultValue("0") Integer page);

    @ApiOperation("Gets an specific experiement")
    @Path("/{id}")
    ExperimentResource getExperiment(@PathParam("id") Long id);
}
