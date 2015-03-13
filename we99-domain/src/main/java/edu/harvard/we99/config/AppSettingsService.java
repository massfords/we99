package edu.harvard.we99.config;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * @author mford
 */
@Api(value = "/settings",
        description = "Service basic settings for the app")
@Path("/settings")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface AppSettingsService {
    /**
     * Gets the current email config
     * @return EmailConf
     */
    @GET
    @Path("/email/config")
    @ApiOperation("Gets the current email config")
    @PreAuthorize("hasRole('PERM_READ_SETTINGS')")
    EmailConfig getEmail();

    /**
     * Updates the email config
     * @statuscode 200 on success
     */
    @POST
    @Path("/email/config")
    @ApiOperation("Updates the email config")
    @PreAuthorize("hasRole('PERM_MODIFY_SETTINGS')")
    Response setEmail(EmailConfig config);

    /**
     * Gets the current email filter
     * @return EmailConf
     */
    @GET
    @Path("/email/filter")
    @ApiOperation("Gets the current email filter")
    @PreAuthorize("hasRole('PERM_READ_SETTINGS')")
    EmailFilter getEmailFilter();

    /**
     * Updates the email config
     * @statuscode 200 on success
     */
    @POST
    @Path("/email/filter")
    @ApiOperation("Updates the email filter")
    @PreAuthorize("hasRole('PERM_MODIFY_SETTINGS')")
    Response setEmailFilter(EmailFilter filter);
}
