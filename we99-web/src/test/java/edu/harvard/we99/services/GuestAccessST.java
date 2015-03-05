package edu.harvard.we99.services;

import edu.harvard.we99.domain.Compound;
import edu.harvard.we99.util.ClientFactory;
import org.junit.Test;

import javax.ws.rs.WebApplicationException;
import java.net.URL;

import static edu.harvard.we99.test.BaseFixture.name;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * @author mford
 */
public class GuestAccessST {

    @Test
    public void test() throws Exception {

        ClientFactory cf = new ClientFactory(new URL(WebAppIT.WE99_URL),
                "we99.2015@example", "pass");
        CompoundService cs = cf.create(CompoundService.class);
        try {
            cs.create(new Compound(name("foo")));
            fail("expected to get an unauthorized exception since the Guest role can't modify anything");
        } catch(WebApplicationException e) {
            assertEquals("Expected to get a Forbidden exception", 401, e.getResponse().getStatus());
        }
    }
}
