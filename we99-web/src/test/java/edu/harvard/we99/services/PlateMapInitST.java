package edu.harvard.we99.services;

import edu.harvard.we99.domain.*;
import edu.harvard.we99.test.Scrubbers;
import edu.harvard.we99.util.ClientFactory;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.net.URL;
import java.util.List;
import java.util.function.Function;

import static edu.harvard.we99.test.BaseFixture.*;
import static edu.harvard.we99.util.JacksonUtil.toJsonString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Integration tests for the PlateMap Sample Data initialization
 *
 * @author SaZ
 */
public class PlateMapInitST {
    private static PlateMapService plateMapService;


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

        plateMapService = cf.create(PlateMapService.class);
    }

    @AfterClass
    public static void tearDown() throws Exception {
        plateMapService = null;
    }

    /**
     *  Checks that the sample initialization had created platemaps
     * @throws Exception
     */
    @Test
    public void platesWereCreated() throws Exception {
        List<PlateMap> plateMaps = plateMapService.listAll(0,999,999).getValues();
        assertTrue(plateMaps.size() > 0);
//        String actual = toJsonString(pt);
//        assertJsonEquals(load("/PlateMapIT/create.json"), actual, jsonScrubber);
    }

//    @Test
//    public void update() throws Exception {
//        // create a new template
//        // update one or more values and wells
//        // assert the updated values
//        PlateMap plateMap = createPlateMap();
//        PlateMap pm = plateMapService.create(plateMap);
//
//        pm.setDescription("my modified description");
//        Coordinate coordinate = new Coordinate(0, 0);
//        WellMap well = new WellMap(coordinate)
//                .withLabel("loc", "well 0,0")
//                .setType(WellType.COMP);
//        pm.getWells().put(coordinate,well);
//        PlateMap updated = plateMapService.update(pm.getId(), pm);
//        String actual = toJsonString(updated);
//        assertJsonEquals(load("/PlateMapIT/updated.json"), actual, jsonScrubber);
//    }
//
//    private PlateMap createPlateMap() {
//        return new PlateMap()
//                .setName(name("plateMap-"))
//                .setDescription("my test plate")
//                .setDim(plateType.getDim());
//    }
}
