package edu.harvard.we99.services;

import edu.harvard.we99.domain.Coordinate;
import edu.harvard.we99.domain.PlateDimension;
import edu.harvard.we99.domain.PlateMap;
import edu.harvard.we99.domain.PlateType;
import edu.harvard.we99.domain.WellMap;
import edu.harvard.we99.domain.WellType;
import edu.harvard.we99.test.Scrubbers;
import edu.harvard.we99.util.ClientFactory;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.net.URL;
import java.util.function.Function;

import static edu.harvard.we99.test.BaseFixture.assertJsonEquals;
import static edu.harvard.we99.test.BaseFixture.load;
import static edu.harvard.we99.test.BaseFixture.name;
import static edu.harvard.we99.util.JacksonUtil.toJsonString;
import static org.junit.Assert.assertNotNull;

/**
 * Integration tests for the PlateTemplate service.
 *
 * This test assumes that the server is running. You can run it as part of the
 * WebAppIT test or via command line in maven.
 *
 * It's also possible to start the Jetty server manually (or any server at the
 * configured endpoint) and run the test against that instance.
 *
 * @author mford
 */
public class PlateMapST {

    /**
     * Proxy to the remote service
     */
    private static PlateMapService plateMapService;

    /**
     * PlateType that we should use when creating the templates. We'll assign
     * this value in our BeforeClass
     */
    private static PlateType plateType;

    /**
     * JSON scrubbers to clean the payloads for our assertions
     */
    private Function<String,String> jsonScrubber =
            Scrubbers.pkey.andThen(Scrubbers.uuid);

    /**
     * Init our proxies and then create a new PlateType for use in the template
     * @throws Exception
     */
    @BeforeClass
    public static void init() throws Exception {
        URL url = new URL(WebAppIT.WE99_URL);
        ClientFactory cf = new ClientFactory(url, WebAppIT.WE99_EMAIL, WebAppIT.WE99_PW);

        // install some plate types
        // install some compounds

        plateMapService = cf.create(PlateMapService.class);

        PlateTypeService plateTypeService = cf.create(PlateTypeService.class);
        plateType = plateTypeService.create(new PlateType()
                .setName(name("plateType"))
                .setDim(new PlateDimension(4, 3))
                .setManufacturer("Foo Inc."));
    }

    @AfterClass
    public static void tearDown() throws Exception {
        // delete all of the templates in the system
        // delete all of the plate types
        // delete all of the compounds

        plateMapService = null;
        plateType = null;
    }

    /**
     * Makes a REST PUT call to create a new PlateTemplate and then asserts that
     * the value it got back matches what we expected to see.
     * @throws Exception
     */
    @Test
    public void create() throws Exception {
        // create a new template
        // assert that it comes back
        // assert that we can get it

        // Yes, this is making a REST PUT call, even though it looks like a simple Java call
        PlateMap plateMap = createPlateMap();
        PlateMap pt = plateMapService.create(plateMap);
        assertNotNull(pt);

        String actual = toJsonString(pt);
        assertJsonEquals(load("/PlateMapIT/create.json"), actual, jsonScrubber);

    }

    @Test
    public void update() throws Exception {
        // create a new template
        // update one or more values and wells
        // assert the updated values
        PlateMap plateMap = createPlateMap();
        PlateMap pm = plateMapService.create(plateMap);

        pm.setDescription("my modified description");
        Coordinate coordinate = new Coordinate(0, 0);
        WellMap well = new WellMap(coordinate)
                .withLabel("loc", "well 0,0")
                .setType(WellType.COMP);
        pm.getWells().put(coordinate,well);
        PlateMap updated = plateMapService.update(pm.getId(), pm);
        String actual = toJsonString(updated);
        assertJsonEquals(load("/PlateMapIT/updated.json"), actual, jsonScrubber);
    }

    private PlateMap createPlateMap() {
        return new PlateMap()
                .setName(name("plateMap-"))
                .setDescription("my test plate")
                .setDim(plateType.getDim());
    }
}
