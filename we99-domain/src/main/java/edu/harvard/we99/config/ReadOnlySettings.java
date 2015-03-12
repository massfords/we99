package edu.harvard.we99.config;

/**
 * @author mford
 */
public interface ReadOnlySettings {
    EmailConfig getEmail();

    EmailFilter getEmailFilter();
}
