package edu.harvard.we99.services;

import edu.harvard.we99.security.User;
import edu.harvard.we99.security.ForgotPasswordService;
import edu.harvard.we99.test.Scrubbers;
import edu.harvard.we99.util.ClientFactory;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.jvnet.mock_javamail.Mailbox;

import javax.mail.Message;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URL;
import java.util.function.Function;

import static edu.harvard.we99.test.BaseFixture.assertJsonEquals;
import static edu.harvard.we99.test.BaseFixture.extractUUID;
import static edu.harvard.we99.test.BaseFixture.load;
import static edu.harvard.we99.util.JacksonUtil.toJsonString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Tests the workflow for a user resetting their password.
 *
 * Note: This test looks very similar to the CreateAccountServiceST. This is because
 * both of these services have similar workflows. I'm ok w/ the close overlap since
 * this isn't an enduring part of the system. The hope is that a second pass
 * through the user authentication/authorization layer is done in order to replace
 * this with a more capable 3rd party identity management system.
 *
 * @author mford
 */
public class ForgotPasswordServiceST {
    private static final HttpServletRequest request = null;

    @Test
    public void test() throws Exception {
        // 1. clear out the mailbox
        // 2. issue a forgot pw call for our baked in user email
        // 3. assert that the email received has the link in it
        // 4. assert that we can verify the email/link
        // 5. set the password for the account
        // 6. get our user bean to verify that we now have access

        // 1. clear out the mailbox
        Mailbox.clearAll();

        // notice that no password is needed
        ClientFactory cf = new ClientFactory(new URL(WebAppIT.WE99_URL), null, null);
        cf.setMediaType(MediaType.APPLICATION_FORM_URLENCODED_TYPE);
        ForgotPasswordService fps = cf.create(ForgotPasswordService.class);

        // 2. hit the forgot password service
        String email = "we99.2015@gmail.com";
        Response response = fps.sendPasswordEmail(email, request);
        assertEquals(200, response.getStatus());

        // 3. assert that the email received has the link in it
        Mailbox messages = Mailbox.get(email);
        assertEquals(1, messages.size());
        Message message = messages.get(0);
        String body = IOUtils.toString(message.getInputStream());
        String expected = load("/ForgotPasswordServiceST/body.txt");
        assertEquals(expected, Scrubbers.uuid.apply(body));

        // 4. assert that we can verify the new account
        String uuid = extractUUID(body);
        User user = fps.verifyResetInfo(uuid, email);
        assertNotNull(user);
        Function<String, String> scrubber = Scrubbers.uuid
                .andThen(Scrubbers.pkey).andThen(Scrubbers.perms);
        assertJsonEquals(load("/ForgotPasswordServiceST/user.json"),
                toJsonString(user), scrubber);

        // 5. set the password for the account
        String password = "pass";
        Response actived = fps.setNewPassword(uuid, email, password);
        assertEquals(200, actived.getStatus());

        // 6. get our user bean to verify that we now have access
        cf = new ClientFactory(new URL(WebAppIT.WE99_URL), email, password);
        UserService userService = cf.create(UserService.class);
        User me = userService.whoami();
        assertNotNull(me);
        assertJsonEquals(load("/ForgotPasswordServiceST/user.json"),
                toJsonString(me), scrubber);
    }
}
