package edu.harvard.we99.services;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import edu.harvard.we99.domain.Compound;
import edu.harvard.we99.domain.lists.Compounds;
import org.springframework.security.access.prepost.PreAuthorize;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * REST Service for performing basic CRUD operations on a Compound
 *
 * @author mford
 */
@Api(value = "/compound",
        description = "Service for performing basic CRUD operations on a Compound")
@Path("/compound")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface CompoundService {
    /**
     * Creates a new compound in our system.
     * @param compound New Compound to add into the system. Name must be unique
     * @return Newly created Compound
     * @statuscode 415 If the compound name is not unique
     */
    @PUT
    @ApiOperation("Create a new Compound")
    @PreAuthorize("hasRole('PERM_MODIFY_COMPOUNDS')")
    Compound create(Compound compound);

    /**
     * Gets an existing compound or throws an exception with 404
     * @param id Compound's id field
     * @return Compound
     * @statuscode 404 If there is no Compound found with this id
     */
    @GET
    @Path("/{id}")
    @ApiOperation("Gets an existing compound or throws an exception with 404")
    @PreAuthorize("hasRole('PERM_READ_COMPOUNDS')")
    @Consumes(MediaType.MEDIA_TYPE_WILDCARD)
    Compound get(@PathParam("id")Long id);

    /**
     * Updates an existing compound or throws an exception with a 404 if not found.
     * @param id Compound's id field
     * @param compound Updated Compound to save
     * @return
     * @statuscode 415 If the compound name is changed to be the same as an existing one
     */
    @POST
    @Path("/{id}")
    @ApiOperation("Updates an existing compound or throws an exception with a 404 if not found.")
    @PreAuthorize("hasRole('PERM_MODIFY_COMPOUNDS')")
    Compound update(@PathParam("id") Long id, Compound compound);

    /**
     * Deletes an existing compound or throws an exception with a 404 if not found
     * @param id Compound's id field
     * @return
     * @statuscode 200 If the Compound was deleted
     * @statuscode 404 If there is no Compound found with this id
     */
    @DELETE
    @Path("/{id}")
    @ApiOperation("Deletes an existing compound or throws an exception with a 404 if not found")
    @Consumes(MediaType.MEDIA_TYPE_WILDCARD)
    @PreAuthorize("hasRole('PERM_MODIFY_COMPOUNDS')")
    Response delete(@PathParam("id") Long id);

    /**
     * Gets the list of the compounds in the system
     */
    @GET
    @ApiOperation("Gets the list of compounds")
    @PreAuthorize("hasRole('PERM_READ_COMPOUNDS')")
    Compounds listAll(@QueryParam("page") @DefaultValue("0") Integer page,
                      @QueryParam("q") @DefaultValue("") String query);
}
