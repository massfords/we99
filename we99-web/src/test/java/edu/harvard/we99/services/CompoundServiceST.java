package edu.harvard.we99.services;

import edu.harvard.we99.domain.Compound;
import edu.harvard.we99.domain.lists.Compounds;
import edu.harvard.we99.test.LogTestRule;
import edu.harvard.we99.test.Scrubbers;
import edu.harvard.we99.util.ClientFactory;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import javax.ws.rs.core.Response;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

import static edu.harvard.we99.test.BaseFixture.assertJsonEquals;
import static edu.harvard.we99.test.BaseFixture.load;
import static edu.harvard.we99.test.BaseFixture.name;
import static edu.harvard.we99.util.JacksonUtil.toJsonString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author mford
 */
public class CompoundServiceST {
    @Rule
    public LogTestRule logTestRule = new LogTestRule();

    private static CompoundService compoundService;
    private Set<Long> deleteMe = new HashSet<>();

    @BeforeClass
    public static void init() throws Exception {
        URL url = new URL(WebAppIT.WE99_URL);
        ClientFactory cf = new ClientFactory(url, WebAppIT.WE99_EMAIL, WebAppIT.WE99_PW);

        compoundService = cf.create(CompoundService.class);
    }

    @AfterClass
    public static void tearDown() throws Exception {
        compoundService = null;
    }

    @After
    public void cleanup() throws Exception {
        for(Long id : deleteMe) {
            Response r = compoundService.delete(id);
            assertEquals(200, r.getStatus());
        }
    }


    @Test
    public void create() throws Exception {
        Compound created = compoundService.create(new Compound(name("comp-")));
        assertNotNull(created);
        deleteMe.add(created.getId());
    }

    @Test
    public void update() throws Exception {
        Compound created = compoundService.create(new Compound(name("comp-")));
        deleteMe.add(created.getId());
        String newName = name("new-");
        Compound updated = compoundService.update(created.getId(), new Compound(newName));
        assertEquals(created.getId(), updated.getId());
        assertEquals(newName, updated.getName());
    }

    @Test
    public void list() throws Exception{

        Compounds initial = compoundService.listAll(0, null, null);


        for(int i=0; i<10; i++) {
            Compound created = compoundService.create(new Compound(name("comp-A-")));
            deleteMe.add(created.getId());
        }
        Compounds list = compoundService.listAll(0, null, null);
        assertEquals(100, list.size());
        assertEquals(10 + initial.getTotalCount(), list.getTotalCount().longValue());
    }

    @Test
    public void typeAhead() throws Exception {
        // results for the user typing 'Ammoni' will match on that string anywhere in the name
        Compounds comps = compoundService.listAll(0, null, "Ammoni");
        assertJsonEquals(load("/CompoundServiceST/list-ammoni.json"),
                toJsonString(comps), Scrubbers.pkey);

        comps = compoundService.listAll(0, null, "dichromate");
        assertJsonEquals(load("/CompoundServiceST/list-dichromate.json"),
                toJsonString(comps), Scrubbers.pkey);
    }

}
