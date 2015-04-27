package edu.harvard.we99.config;

/**
 * Read only version of the app settings. This is used by services that need
 * to access system settings but not change them.
 *
 * @author mford
 */
public interface ReadOnlySettings {

    /**
     * Getter for the email config
     * @return
     */
    EmailConfig getEmail();

    /**
     * Getter for the email filter.
     * @return
     */
    EmailFilter getEmailFilter();
}
