package edu.harvard.we99.services;

import edu.harvard.we99.domain.lists.Users;
import edu.harvard.we99.security.ManageUserService;
import edu.harvard.we99.security.RoleName;
import edu.harvard.we99.security.User;
import edu.harvard.we99.util.ClientFactory;
import org.junit.Before;
import org.junit.Test;

import java.net.URL;

import static org.junit.Assert.assertEquals;

/**
 * @author mford
 */
public class ManageUserServiceST {

    private UserService us;
    private ManageUserService mus;

    @Before
    public void init() throws Exception {

        ClientFactory cf = new ClientFactory(new URL(WebAppIT.WE99_URL),
                WebAppIT.WE99_EMAIL, WebAppIT.WE99_PW);

        us = cf.create(UserService.class);
        mus = cf.create(ManageUserService.class);
    }


    @Test
    public void test() throws Exception {

        // get the guest user
        User guestUser = findGuestUser();

        // record their current role
        RoleName roleName = guestUser.getRole().getName();
        assertEquals(RoleName.BuiltIn.Guest.asName(), roleName);

        // change their role
        mus.asignRole(guestUser.getId(), RoleName.BuiltIn.Scientist.asName());

        // verify the change
        User postUpdate = findGuestUser();
        assertEquals(RoleName.BuiltIn.Scientist.asName(), postUpdate.getRole().getName());

        // change them back
        mus.asignRole(guestUser.getId(), RoleName.BuiltIn.Guest.asName());
        User finalUpdate = findGuestUser();
        assertEquals(RoleName.BuiltIn.Guest.asName(), finalUpdate.getRole().getName());
    }

    private User findGuestUser() {
        Users found = us.list(null, null, "Guest User");
        assertEquals(1, found.size());
        return found.getValues().get(0);
    }

}
