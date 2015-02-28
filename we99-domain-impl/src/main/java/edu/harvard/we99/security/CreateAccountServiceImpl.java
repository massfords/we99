package edu.harvard.we99.security;

import edu.harvard.we99.domain.User;
import edu.harvard.we99.services.storage.UserStorage;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.text.StrSubstitutor;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.PersistenceException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

/**
 * Implementation of the service to create new user accounts. Manages the simple
 * workflow for users signing up to access the system.
 *
 * @author mford
 */
public class CreateAccountServiceImpl implements CreateAccountService {

    private static final Logger log = LoggerFactory.getLogger(CreateAccountServiceImpl.class);

    /**
     * Used to fetch User entities
     */
    private final UserStorage storage;

    /**
     * Config for sending emails. This is something that should be configured
     * at install time.
     */
    private final EmailConfig emailConfig;

    /**
     * Filter to accepting new user registrations. The provided email must
     * satisfy this filter. If not, the account registration is rejected.
     */
    private final EmailFilter emailFilter;

    public CreateAccountServiceImpl(
            EmailFilter emailFilter,
            EmailConfig emailConfig,
            UserStorage storage) {
        this.storage = storage;
        this.emailConfig = emailConfig;
        this.emailFilter = emailFilter;
    }

    @Override
    public Response createAccount(String emailAddress,
                                  String firstName,
                                  String lastName,
                                  HttpServletRequest request) {
        // insert a new user using the given params
        // if successful, send them an email with the password as a link
        // if not successful, return an error code

        if (!emailFilter.test(emailAddress)) {
            log.error("email disallowed by filter: {}", emailAddress);
            throw new WebApplicationException(Response.status(403).build());
        }

        try {

            User user = storage.create(new User(emailAddress, firstName, lastName));

            Email email = createEmail();
            email.setSubject("Welcome to WE99");

            String emailBody = prepareEmailMessage(request, user);

            email.setMsg(emailBody);
            email.addTo(emailAddress);
            email.send();

            return Response.temporaryRedirect(new URI("../../open/registrationSent.html")).build();
        } catch(PersistenceException | ConstraintViolationException e) {
            return Response.status(409).build();
        } catch (EmailException e) {
            log.error("Error sending email to {}", emailAddress, e);
            return Response.serverError().build();
        } catch (IOException e) {
            log.error("Error loading email resource. Check the classpath for newUserEmail.txt", e);
            return Response.serverError().build();
        } catch (URISyntaxException e) {
            // seems like we would never get this.
            log.error("can't build the uri?", e);
            return Response.ok().build();
        }
    }

    @Override
    public User activateAccount(String uuid, String email) {
        try {
            return storage.findByUUID(uuid, email);
        } catch(Exception e) {
            log.error("error finding user with reg key {}", uuid, e);
            throw new WebApplicationException(Response.status(404).build());
        }
    }

    @Override
    public Response activateAccount(String uuid, String email, String password) {
        try {
            storage.activate(uuid, email, password);
            return Response.ok().build();
        } catch(Exception e) {
            log.error("error finding user with reg key {}", uuid, e);
            throw new WebApplicationException(Response.status(404).build());
        }
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
    private String prepareEmailMessage(HttpServletRequest request, User user) throws IOException {
        Map<String,String> params = new HashMap<>();
        params.put("firstName", user.getFirstName());
        params.put("lastName", user.getLastName());
        params.put("email", user.getEmail());
        params.put("protocol", request.isSecure()? "https" : "http");
        params.put("host", request.getServerName());
        params.put("port", String.valueOf(request.getServerPort()));
        params.put("context", request.getContextPath().substring(1));
        params.put("uuid", user.getPassword());

        StrSubstitutor subby = new StrSubstitutor(params);
        String template = loadTemplate();
        return subby.replace(template);
    }

    private String loadTemplate() throws IOException {
        try (InputStream is = getClass().getResourceAsStream("/newUserEmail.txt")) {
            return IOUtils.toString(is);
        }
    }

    /**
     * Using commons-mail to create and deliver the mail. It's a little cleaner
     * than the JavaMail API
     * @return
     * @throws EmailException
     */
    private Email createEmail() throws EmailException {
        Email email = new SimpleEmail();
//        email.setDebug(true);
        email.setHostName(this.emailConfig.getHost());
        email.setSmtpPort(this.emailConfig.getPort());
        email.setAuthenticator(new DefaultAuthenticator(this.emailConfig.getUsername(), this.emailConfig.getPassword()));
        email.setStartTLSEnabled(true);
        email.setFrom(emailConfig.getFrom());
        return email;
    }
}
