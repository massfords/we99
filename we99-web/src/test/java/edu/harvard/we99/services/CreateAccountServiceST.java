package edu.harvard.we99.services;

import edu.harvard.we99.security.CreateAccountService;
import edu.harvard.we99.security.User;
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

import static edu.harvard.we99.test.BaseFixture.assertJsonEquals;
import static edu.harvard.we99.test.BaseFixture.extractUUID;
import static edu.harvard.we99.test.BaseFixture.load;
import static edu.harvard.we99.test.BaseFixture.name;
import static edu.harvard.we99.util.JacksonUtil.toJsonString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Tests the workflow for creating a new user account.
 * @author mford
 */
public class CreateAccountServiceST {
    private static final HttpServletRequest request = null;

    @Test
    public void test() throws Exception {
        // 1. clear out the mailbox
        // 2. register a new email address
        // 3. assert that the email received has the link in it
        // 4. assert that we can verify the new account
        // 5. set the password for the account
        // 6. get our user bean to verify that we now have access

        // 1. clear out the mailbox
        Mailbox.clearAll();

        // notice that no password is needed
        ClientFactory cf = new ClientFactory(new URL(WebAppIT.WE99_URL), null, null);
        cf.setMediaType(MediaType.APPLICATION_FORM_URLENCODED_TYPE);
        CreateAccountService cas = cf.create(CreateAccountService.class);

        // 2. register a new email address
        String email = name("junit@");
        Response response = cas.createAccount(email,
                "Mark", "Ford", request);
        assertEquals(307, response.getStatus());

        // 3. assert that the email received has the link in it
        Mailbox messages = Mailbox.get(email);
        assertEquals(1, messages.size());
        Message message = messages.get(0);
        String body = IOUtils.toString(message.getInputStream());
        String expected = load("/CreateAccountServiceST/body.txt");
        assertEquals(expected, Scrubbers.uuid.apply(body));

        // 4. assert that we can verify the new account
        String uuid = extractUUID(body);
        User user = cas.activateAccount(uuid, email);
        assertNotNull(user);
        assertJsonEquals(load("/CreateAccountServiceST/user.json"),
                toJsonString(user), Scrubbers.uuid.andThen(Scrubbers.pkey));

        // 5. set the password for the account
        String password = "password1234";
        Response actived = cas.activateAccount(uuid, email, password);
        assertEquals(200, actived.getStatus());

        // 6. get our user bean to verify that we now have access
        cf = new ClientFactory(new URL(WebAppIT.WE99_URL), email, password);
        UserService userService = cf.create(UserService.class);
        User me = userService.whoami();
        assertNotNull(me);
        assertJsonEquals(load("/CreateAccountServiceST/user.json"),
                toJsonString(me), Scrubbers.uuid.andThen(Scrubbers.pkey));
    }
}