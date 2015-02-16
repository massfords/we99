package edu.harvard.we99.services;

import edu.harvard.we99.domain.PlateTemplate;
import edu.harvard.we99.domain.PlateType;
import edu.harvard.we99.test.PrimaryKeyScrubber;
import edu.harvard.we99.test.UUIDScrubber;
import edu.harvard.we99.util.ClientFactory;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.net.URL;
import java.util.UUID;
import java.util.function.Function;

import static edu.harvard.we99.test.BaseFixture.assertJsonEquals;
import static edu.harvard.we99.test.BaseFixture.load;
import static edu.harvard.we99.util.JacksonUtil.toJsonString;
import static org.junit.Assert.assertNotNull;

/**
 * Integration tests for the PlateTemplate service
 *
 * @author mford
 */
public class PlateTemplateIT {

    /**
     * Proxy to the remote service
     */
    private static PlateTypeService plateTypeService;

    /**
     * Proxy to the remote service
     */
    private static PlateTemplateService plateTemplateService;

    /**
     * PlateType that we should use when creating the templates. We'll assign
     * this value in our BeforeClass
     */
    private static PlateType plateType;

    /**
     * JSON scrubbers to clean the payloads for our assertions
     */
    private Function<String,String> jsonScrubber =
            new PrimaryKeyScrubber().andThen(new UUIDScrubber());

    /**
     * Init our proxies and then create a new PlateType for use in the template
     * @throws Exception
     */
    @BeforeClass
    public static void init() throws Exception {
        URL url = new URL("http://localhost:8080/we99/services/rest/");
        ClientFactory cf = new ClientFactory(url);

        // install some plate types
        // install some compounds

        plateTypeService = cf.create(PlateTypeService.class);
        plateTemplateService = cf.create(PlateTemplateService.class);

        plateType = plateTypeService.create(new PlateType()
                .withRows(4).withCols(3)
                .withManufacturer("Foo Inc."));
    }

    @AfterClass
    public static void tearDown() throws Exception {
        // delete all of the templates in the system
        // delete all of the plate types
        // delete all of the compounds

        plateTypeService = null;
        plateTemplateService = null;
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
        PlateTemplate pt = plateTemplateService.create(new PlateTemplate()
                        .withName("plateTemplate-" + UUID.randomUUID().toString())
                        .withDescription("my test plate")
                        .withPlateType(plateType)
        );
        assertNotNull(pt);

        String actual = toJsonString(pt);
        assertJsonEquals(load("/PlateTemplateIT/create.json"), actual, jsonScrubber);

    }

//    @Test
//    public void update() throws Exception {
//        // create a new template
//        // update one or more valus on it
//        // assert the updated values
//    }

//    @Test
//    public void delete() throws Exception {
//        // create a new template
//        // delete it
//        // assert that we get a 404 if we try to get it
//    }
}
