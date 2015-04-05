package edu.harvard.we99.services;

import edu.harvard.we99.domain.Compound;
import edu.harvard.we99.domain.Dose;
import edu.harvard.we99.domain.Experiment;
import edu.harvard.we99.domain.Plate;
import edu.harvard.we99.domain.Protocol;
import edu.harvard.we99.domain.Well;
import edu.harvard.we99.domain.lists.Experiments;
import edu.harvard.we99.domain.lists.Users;
import edu.harvard.we99.domain.results.ResultStatus;
import edu.harvard.we99.domain.results.StatusChange;
import edu.harvard.we99.security.User;
import edu.harvard.we99.services.experiments.ExperimentResource;
import edu.harvard.we99.services.experiments.MemberResource;
import edu.harvard.we99.services.experiments.PlatesResource;
import edu.harvard.we99.test.DisableLogging;
import edu.harvard.we99.util.ClientFactory;
import org.apache.cxf.interceptor.AbstractFaultChainInitiatorObserver;
import org.apache.cxf.phase.PhaseInterceptorChain;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static edu.harvard.we99.test.BaseFixture.assertOk;
import static edu.harvard.we99.test.BaseFixture.name;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * @author mford
 */
public class ExperimentServiceST {

    private static ExperimentService es;
    private static PlateTypeService pts;
    private static ProtocolService ps;
    private static User user;
    private static PlateResultsFixture resultsFixture;
    private Experiment xp;


    @BeforeClass
    public static void init() throws Exception {
        URL url = new URL(WebAppIT.WE99_URL);
        ClientFactory cf = new ClientFactory(url, WebAppIT.WE99_EMAIL, WebAppIT.WE99_PW);

        es = cf.create(ExperimentService.class);
        user = cf.create(UserService.class).list(null, null, "we99.2015@example").getValues().get(0);
        pts = cf.create(PlateTypeService.class);
        ps = cf.create(ProtocolService.class);
        resultsFixture = new PlateResultsFixture();
    }

    @AfterClass
    public static void tearDown() throws Exception {
        es = null;
        user = null;
        resultsFixture = null;
        pts = null;
    }

    @Before
    public void setUp() throws Exception {
        xp = es.create(
                new Experiment(name("Experiment")).setProtocol(new Protocol(name("p")))
        );
    }

    @Test
    public void typeAhead() throws Exception {
        Experiments experiments = es.listExperiments(null, null, xp.getName());
        assertEquals(1, experiments.size());
    }

    @Test
    public void listPlates() throws Exception {
        // no assertions here, just making sure we can invoke all w/o exceptions
        Set<Compound> compounds = new HashSet<>();
        for(Experiment e : es.listExperiments(null, null, null).getValues()) {
            ExperimentResource er = es.getExperiment(e.getId());
            PlatesResource pr = er.getPlates();
            for(Plate p : pr.list(null, null,null).getValues()) {
                for(Well well : p.getWells().values()) {
                    compounds.addAll(well.getContents()
                            .stream()
                            .map(Dose::getCompound)
                            .collect(Collectors.toList()));
                }
            }
        }
        System.out.println("# of compounds found :" + compounds.size());
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

    @Test
    public void delete() throws Exception {
        Long id = xp.getId();
        Response r = es.getExperiment(id).delete();
        assertEquals(200, r.getStatus());
    }

    @Test
    public void published_are_immutable() throws Exception {
        // create experiment
        // create plate
        // import results
        // publish
        // assert we can't make a change
        Plate plate = createSinglePlateExperiment();

        ExperimentResource experimentResource = es.getExperiment(xp.getId());
        experimentResource.publish();

        // should not be able to make changes now
        try {
            DisableLogging.turnOff(PhaseInterceptorChain.class, AbstractFaultChainInitiatorObserver.class);
            experimentResource.getPlates().getPlates(plate.getId())
                    .getPlateResult()
                    .updateStatus(
                            new StatusChange(0, 0, ResultStatus.EXCLUDED));
            fail("expected the experiment to be locked");
        } catch (WebApplicationException e) {
            assertEquals(403, e.getResponse().getStatus());
        } finally {
            DisableLogging.restoreAll();
        }
    }

    @Test
    public void experimentVisibility() throws Exception {
        // main user creates an experiment, guest user can't see it
        createSinglePlateExperiment();

        URL url = new URL(WebAppIT.WE99_URL);
        ClientFactory cf = new ClientFactory(url, "we99.2015@example", WebAppIT.WE99_PW);
        ExperimentService guestExperimentService = cf.create(ExperimentService.class);
        Experiments experiments = guestExperimentService.listExperiments(null, null, null);
        // gues user cannot see the experiment
        assertThat(experiments.getValues()).extracting("id").doesNotContain(xp.getId());

        // main user publishes the experiment, guest user can see it now
        ExperimentResource experimentResource = es.getExperiment(xp.getId());
        experimentResource.publish();

        experiments = guestExperimentService.listExperiments(null, null, null);
        // gues user CAN see the experiment
        assertThat(experiments.getValues()).extracting("id").contains(xp.getId());
    }

    @Test
    public void update_existingProtocol() throws Exception {
        Protocol protocol = ps.create(new Protocol(name("foo")));
        ExperimentResource er = es.getExperiment(xp.getId());
        Experiment updated = er.update(xp.setProtocol(protocol));
        assertEquals(protocol.getName(), updated.getProtocol().getName());
    }

    @Test
    public void update_newProtocol() throws Exception {
        ExperimentResource er = es.getExperiment(xp.getId());
        Protocol foo = new Protocol(name("foo"));
        Experiment updated = er.update(xp.setProtocol(foo));
        assertEquals(foo.getName(), updated.getProtocol().getName());
    }

    private Plate createSinglePlateExperiment() {
        String plateTypeName = pts.listAll(null, null, null).getValues().get(0).getName();
        ExperimentResource experimentResource = es.getExperiment(xp.getId());
        PlateUtils.createPlateFromCSV(xp.getId(), name("plateName"), plateTypeName,
                getClass().getResourceAsStream("/ExperimentServiceST/plate.csv"));
        Plate plate = experimentResource.getPlates().list(null, null, null).getValues().get(0);
        Response r = resultsFixture.postResults(plate, "/ExperimentServiceST/results.csv");
        assertEquals(200, r.getStatus());
        return plate;
    }
}
