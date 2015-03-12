package edu.harvard.we99.config;

import edu.harvard.we99.services.storage.AppSettingsStorage;

/**
 * @author mford
 */
public class ReadOnlySettingsImpl implements ReadOnlySettings {

    private final AppSettingsStorage storage;

    public ReadOnlySettingsImpl(AppSettingsStorage storage) {
        this.storage = storage;
    }


    @Override
    public EmailConfig getEmail() {
        EmailConfig ec = storage.load(EmailConfig.class);
        if (ec == null) {
            ec = new EmailConfig();
        }
        return ec;
    }

    @Override
    public EmailFilter getEmailFilter() {
        EmailFilter ef = storage.load(EmailFilter.class);
        if (ef == null) {
            ef = new EmailFilter();
        }
        return ef;
    }
}
