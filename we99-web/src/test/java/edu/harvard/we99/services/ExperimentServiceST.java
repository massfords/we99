package edu.harvard.we99.services;

import edu.harvard.we99.domain.Experiment;
import edu.harvard.we99.domain.lists.Users;
import edu.harvard.we99.security.User;
import edu.harvard.we99.services.experiments.MemberResource;
import edu.harvard.we99.util.ClientFactory;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.ws.rs.core.Response;
import java.net.URL;

import static edu.harvard.we99.test.BaseFixture.assertOk;
import static edu.harvard.we99.test.BaseFixture.name;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author mford
 */
public class ExperimentServiceST {

    private static ExperimentService es;
    private static User user;
    private Experiment xp;


    @BeforeClass
    public static void init() throws Exception {
        URL url = new URL(WebAppIT.WE99_URL);
        ClientFactory cf = new ClientFactory(url, WebAppIT.WE99_EMAIL, WebAppIT.WE99_PW);

        es = cf.create(ExperimentService.class);
        user = cf.create(UserService.class).find("we99.2015@example").getValues().get(0);
    }

    @AfterClass
    public static void tearDown() throws Exception {
        es = null;
        user = null;
    }

    @Before
    public void setUp() throws Exception {
        xp = es.create(new Experiment(name("Experiment")));
    }

    @Test
    public void addMember() throws Exception {
        MemberResource mr = es.getExperiment(xp.getId()).getMembers();
        Response response = mr.addMember(user.getId());
        assertOk(response);

        Users members = mr.listMembers();
        assertThat(members.getValues()).extracting("email").containsExactly("we99.2015@example", "we99.2015@gmail.com");
    }

    @Test
    public void removeMember() throws Exception {
        MemberResource mr = es.getExperiment(xp.getId()).getMembers();
        Response response = mr.addMember(user.getId());
        assertOk(response);
        response = mr.removeMember(user.getId());
        assertOk(response);

        Users members = mr.listMembers();
        assertThat(members.getValues()).extracting("email").containsExactly("we99.2015@gmail.com");
    }
}
