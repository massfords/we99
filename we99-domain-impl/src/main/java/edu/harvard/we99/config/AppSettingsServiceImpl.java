package edu.harvard.we99.config;

import edu.harvard.we99.services.storage.AppSettingsStorage;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.io.IOException;

/**
 * @author mford
 */
public class AppSettingsServiceImpl implements AppSettingsService {

    private final AppSettingsStorage storage;
    private final ReadOnlySettings readOnlySettings;

    public AppSettingsServiceImpl(AppSettingsStorage storage) {
        this.storage = storage;
        readOnlySettings = new ReadOnlySettingsImpl(storage);
    }

    @Override
    public EmailConfig getEmail() {
        return readOnlySettings.getEmail();
    }

    @Override
    public Response setEmail(EmailConfig config) {
        try {
            storage.store(EmailConfig.class, config);
            return Response.ok().build();
        } catch (IOException e) {
            throw new WebApplicationException(Response.serverError().build());
        }
    }

    @Override
    public EmailFilter getEmailFilter() {
        return readOnlySettings.getEmailFilter();
    }

    @Override
    public Response setEmailFilter(EmailFilter filter) {
        try {
            storage.store(EmailFilter.class, filter);
            return Response.ok().build();
        } catch (IOException e) {
            throw new WebApplicationException(Response.serverError().build());
        }
    }
}
