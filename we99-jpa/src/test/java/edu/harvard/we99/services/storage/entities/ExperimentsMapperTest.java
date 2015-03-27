package edu.harvard.we99.services.storage.entities;

import edu.harvard.we99.domain.Experiment;
import edu.harvard.we99.domain.Protocol;
import edu.harvard.we99.test.EastCoastTimezoneRule;
import org.joda.time.DateTime;
import org.junit.Rule;
import org.junit.Test;

import java.util.Collections;

import static edu.harvard.we99.test.BaseFixture.assertJsonEquals;
import static edu.harvard.we99.test.BaseFixture.load;
import static edu.harvard.we99.util.JacksonUtil.toJsonString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author mford
 */
public class ExperimentsMapperTest {

    @Rule
    public EastCoastTimezoneRule eastCoastTimezoneRule = new EastCoastTimezoneRule();

    Protocol protocol = new Protocol().withId(200L).withName("proto1");
    ProtocolEntity protocolEntity = new ProtocolEntity().setId(200L).setName("proto1");

    @Test
    public void domainToEntity_new() throws Exception {
        Experiment fromCaller = new Experiment().setId(100L).setName("exp")
                .setProtocol(protocol);
        ExperimentEntity ee = Mappers.EXPERIMENTS.mapReverse(fromCaller);
        // id is copied over
        assertEquals(100L, ee.getId().longValue());
        // protocol is copied over
        assertNotNull(ee.getProtocol());
        assertEquals("proto1", ee.getProtocol().getName());
        // name is copied over
        assertEquals("exp", ee.getName());
    }

    @Test
    public void domainToEntity_existing() throws Exception {
        Experiment fromCaller = new Experiment().setId(100L).setName("exp")
                .setProtocol(protocol);
        ExperimentEntity ee = new ExperimentEntity();
        Mappers.EXPERIMENTS.mapReverse(fromCaller, ee);
        // id is copied over
        assertEquals(100L, ee.getId().longValue());
        // protocol is copied over
        assertNotNull(ee.getProtocol());
        assertEquals("proto1", ee.getProtocol().getName());
        // name is copied over
        assertEquals("exp", ee.getName());
    }

    @Test
    public void entityToDomain() throws Exception {
        ExperimentEntity ee = new ExperimentEntity()
                .setId(500L)
                .setName("exp")
                .setProtocol(protocolEntity)
                .setCreated(new DateTime("2015-01-02T10:30:20Z"))
                .setLabels(Collections.singleton(new LabelEntity("foo", "bar")))
                .setMembers(Collections.singletonMap("foo@example.com",
                        new UserEntity("foo@example.com", "Foo", "User")
                                .setId(200L)));
        Experiment e = Mappers.EXPERIMENTS.map(ee);
        assertJsonEquals(load("/Mappers/experiment.json"), toJsonString(e));
    }
}
