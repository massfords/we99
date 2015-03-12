package edu.harvard.we99.security;

import edu.harvard.we99.config.EmailConfig;
import edu.harvard.we99.config.ReadOnlySettings;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Map;

/**
 * An internal service for use by other services that need to send emails. This
 * provides for some basic functionality around the concept of email templates
 * and also provides a central point for configuring our SMTP channel.
 * @author mford
 */
public class InternalEmailService {

    /**
     * Config for sending emails. This is something that should be configured
     * at install time.
     */
    private final ReadOnlySettings settings;

    public InternalEmailService(ReadOnlySettings settings) {
        this.settings = settings;
    }

    /**
     * Using commons-mail to create and deliver the mail. It's a little cleaner
     * than the JavaMail API
     * @return
     * @throws org.apache.commons.mail.EmailException
     */
    public Email createEmail() throws EmailException {

        EmailConfig emailConfig = settings.getEmail();

        if (StringUtils.isEmpty(emailConfig.getHost())) {
            throw new EmailException("email service not configured");
        }

        Email email = new SimpleEmail();
//        email.setDebug(true);
        email.setHostName(emailConfig.getHost());
        email.setSmtpPort(emailConfig.getPort());
        email.setAuthenticator(new DefaultAuthenticator(emailConfig.getUsername(), emailConfig.getPassword()));
        email.setStartTLSEnabled(true);
        email.setFrom(emailConfig.getFrom());
        return email;
    }

    public static void populateRequestProps(HttpServletRequest request, Map<String,String> params) {
        params.put("protocol", request.isSecure()? "https" : "http");
        params.put("host", request.getServerName());
        params.put("port", String.valueOf(request.getServerPort()));
        params.put("context", request.getContextPath().substring(1));
    }

    public static void populateUserProps(User user, Map<String,String> params) {
        params.put("firstName", user.getFirstName());
        params.put("lastName", user.getLastName());
        params.put("email", user.getEmail());
    }

    /**
     * Prepares the body of the email. A simple replacement of tokens in a template
     * to form a greeting and link for the user to click on.
     *
     * This is very basic. A better solution would be a 3rd party identity
     * management system that handled user registration.
     *
     * @param request
     * @param user
     * @return
     * @throws IOException
     */
    public static String prepareEmailMessage(
            HttpServletRequest request,
            User user,
            EmailTemplates template,
            Map<String,String> params) throws IOException {
        populateRequestProps(request, params);
        populateUserProps(user, params);
        return template.format(params);
    }

}
