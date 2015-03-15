package edu.harvard.we99.security;

import edu.harvard.we99.config.EmailFilter;
import edu.harvard.we99.config.ReadOnlySettings;
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

    private final ReadOnlySettings settings;

    public CreateAccountServiceImpl(
            ReadOnlySettings settings,
            InternalEmailService emailService,
            UserStorage storage) {
        this.storage = storage;
        this.emailService = emailService;
        this.settings = settings;
    }

    @Override
    public Response createAccount(User createMe,
                                  HttpServletRequest request) {
        // insert a new user using the given params
        // if successful, send them an email with the password as a link
        // if not successful, return an error code

        EmailFilter emailFilter = settings.getEmailFilter();

        if (!emailFilter.test(createMe.getEmail())) {
            log.error("email disallowed by filter: {}", createMe.getEmail());
            throw new WebApplicationException(Response.status(403).build());
        }

        Long userId = null;
        try {
            User user = storage.create(createMe);
            userId = user.getId();

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
            email.addTo(user.getEmail());
            email.send();

            return Response.ok().build();
        } catch(PersistenceException | ConstraintViolationException e) {
            return Response.status(409).build();
        } catch (EmailException | IOException e) {
            log.error("Error sending email to {}", createMe.getEmail(), e);
            if (userId != null) {
                storage.delete(userId);
            }
            return Response.serverError().build();
        }
    }

    @Override
    public User verifyAccount(String uuid, User user) {
        try {
            return storage.findByUUID(uuid, user.getEmail());
        } catch(Exception e) {
            log.error("error finding user with reg key {}", uuid, e);
            throw new WebApplicationException(Response.status(404).build());
        }
    }

    @Override
    public Response activateAccount(String uuid, User user) {
        try {
            storage.activate(uuid, user.getEmail(), user.getPassword());
            return Response.ok().build();
        } catch(Exception e) {
            log.error("error finding user with reg key {}", uuid, e);
            throw new WebApplicationException(Response.status(404).build());
        }
    }
}
