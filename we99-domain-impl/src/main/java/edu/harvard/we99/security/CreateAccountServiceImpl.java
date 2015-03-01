package edu.harvard.we99.security;

import edu.harvard.we99.domain.User;
import edu.harvard.we99.services.storage.UserStorage;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.PersistenceException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import static edu.harvard.we99.security.InternalEmailService.prepareEmailMessage;

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
     * Helper service for sending emails.
     */
    private final InternalEmailService emailService;

    /**
     * Filter to accepting new user registrations. The provided email must
     * satisfy this filter. If not, the account registration is rejected.
     */
    private final EmailFilter emailFilter;

    public CreateAccountServiceImpl(
            EmailFilter emailFilter,
            InternalEmailService emailService,
            UserStorage storage) {
        this.storage = storage;
        this.emailService = emailService;
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

            Email email = emailService.createEmail();
            email.setSubject("Welcome to WE99");

            // after creating the user, their password is a random UUID. We'll send
            // this to them in an email so they can click on it and then login.
            // in the meantime, this user will never be able to login outside of this
            // workflow since the format of the UUID will NEVER match a value
            // entered by the user through our standard login process.
            // Why never? Because the value entered by the user is salted, hashed,
            // and then encoded to a hex string. Thus, even if they login w/ the
            // exact UUID as their value then it still won't match what we have
            // stored for them.
            String uuid = user.getPassword();
            Map<String,String> params = new HashMap<>();
            params.put("uuid", uuid);

            String emailBody = prepareEmailMessage(request, user,
                    EmailTemplates.newUserEmail, params);

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
}
