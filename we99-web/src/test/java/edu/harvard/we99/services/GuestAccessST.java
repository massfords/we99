package edu.harvard.we99.services;

import edu.harvard.we99.domain.Compound;
import edu.harvard.we99.test.DisableLogging;
import edu.harvard.we99.util.ClientFactory;
import org.apache.cxf.interceptor.AbstractFaultChainInitiatorObserver;
import org.apache.cxf.jaxrs.impl.WebApplicationExceptionMapper;
import org.apache.cxf.phase.PhaseInterceptorChain;
import org.junit.Test;

import javax.ws.rs.WebApplicationException;
import java.net.URL;

import static edu.harvard.we99.test.BaseFixture.name;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * @author mford
 */
public class GuestAccessST {

    @Test
    public void test() throws Exception {

        DisableLogging.turnOff(WebApplicationExceptionMapper.class,
                PhaseInterceptorChain.class,
                AbstractFaultChainInitiatorObserver.class);
        ClientFactory cf = new ClientFactory(new URL(WebAppIT.WE99_URL),
                "we99.2015@example", "pass");
        CompoundService cs = cf.create(CompoundService.class);
        try {
            cs.create(new Compound(name("foo")));
            fail("expected to get an unauthorized exception since the Guest role can't modify anything");
        } catch(WebApplicationException e) {
            // why is this sometimes 401 and sometimes 403?
            int code = e.getResponse().getStatus();
            assertTrue("Expected to get a Forbidden exception", 401==code || 403==code);
        } finally {
            DisableLogging.restoreAll();
        }

    }
}
