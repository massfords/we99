package edu.harvard.we99.security;

import edu.harvard.we99.domain.User;
import edu.harvard.we99.services.storage.UserStorage;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.PersistenceException;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static edu.harvard.we99.security.InternalEmailService.prepareEmailMessage;

/**
 * The workflow for forgot password is similar to account activation and as such
 * this class delegates some of the work to the CreateAccountService.
 *
 * In fact, much of the initial "sendPasswordEmail" is similar enough to perhaps
 * expose some functionality in a base class or util. For now, I've extracted
 * most of the work to static methods in the InternalEmailService helper class.
 *
 * @author mford
 */
public class ForgotPasswordServiceImpl implements ForgotPasswordService {

    private static final Logger log = LoggerFactory.getLogger(ForgotPasswordServiceImpl.class);

    /**
     * Used to fetch User entities
     */
    private final UserStorage storage;

    /**
     * Helper service for sending emails.
     */
    private final InternalEmailService emailService;

    private CreateAccountService cas;

    public ForgotPasswordServiceImpl(UserStorage storage,
                                     InternalEmailService emailService,
                                     CreateAccountService cas) {
        this.storage = storage;
        this.emailService = emailService;
        this.cas = cas;
    }

    @Override
    public Response sendPasswordEmail(String emailAddress, HttpServletRequest request) {
        try {
            User user = storage.findByEmail(emailAddress);
            String uuid = UUID.randomUUID().toString();
            user.setPassword(uuid);
            storage.update(user.getId(), user);

            Email email = emailService.createEmail();
            email.setSubject("WE99 Password Reset");

            Map<String,String> params = new HashMap<>();
            params.put("uuid", uuid);

            String emailBody = prepareEmailMessage(request, user,
                    EmailTemplates.forgotPassword, params);

            email.setMsg(emailBody);
            email.addTo(emailAddress);
            email.send();

            return Response.ok().build();
        } catch(PersistenceException e) {
            throw new WebApplicationException(Response.status(404).build());
        } catch (EmailException e) {
            log.error("Error sending email to {}", emailAddress, e);
            return Response.serverError().build();
        } catch (IOException e) {
            log.error("Error loading email resource. Check the classpath for newUserEmail.txt", e);
            return Response.serverError().build();
        }
    }

    @Override
    public User verifyResetInfo(String uuid, String email) {
        return cas.activateAccount(uuid, email);
    }

    @Override
    public Response setNewPassword(String uuid, String email, String password) {
        return cas.activateAccount(uuid, email, password);
    }
}
