package edu.harvard.we99.services;

import edu.harvard.we99.domain.Compound;
import edu.harvard.we99.domain.Coordinate;
import edu.harvard.we99.domain.Dose;
import edu.harvard.we99.domain.PlateDimension;
import edu.harvard.we99.domain.PlateTemplate;
import edu.harvard.we99.domain.PlateType;
import edu.harvard.we99.domain.Well;
import edu.harvard.we99.domain.WellType;
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
public class PlateTemplateST {

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
     * Compound used for the various wells we're filling
     */
    private static Compound compound;

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
        URL url = new URL(WebAppIT.WE99_URL);
        ClientFactory cf = new ClientFactory(url);

        // install some plate types
        // install some compounds

        plateTemplateService = cf.create(PlateTemplateService.class);

        PlateTypeService plateTypeService = cf.create(PlateTypeService.class);
        plateType = plateTypeService.create(new PlateType()
                .withDim(new PlateDimension(4, 3))
                .withManufacturer("Foo Inc."));

        CompoundService compoundService = cf.create(CompoundService.class);
        compound = compoundService.create(new Compound(UUID.randomUUID().toString()));
    }

    @AfterClass
    public static void tearDown() throws Exception {
        // delete all of the templates in the system
        // delete all of the plate types
        // delete all of the compounds

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
        PlateTemplate plateTemplate = createPlateTemplate();
        PlateTemplate pt = plateTemplateService.create(plateTemplate);
        assertNotNull(pt);

        String actual = toJsonString(pt);
        assertJsonEquals(load("/PlateTemplateIT/create.json"), actual, jsonScrubber);

    }

    @Test
    public void update() throws Exception {
        // create a new template
        // update one or more values and wells
        // assert the updated values
        PlateTemplate plateTemplate = createPlateTemplate();
        PlateTemplate pt = plateTemplateService.create(plateTemplate);

        pt.setDescription("my modified description");
        Coordinate coordinate = new Coordinate(0, 0);
        Well well = new Well(coordinate)
                .withLabel("well 0,0")
                .withType(WellType.MEASURED);
        well.dose(new Dose(compound, 1));
        pt.getWells().put(coordinate,well);
        PlateTemplate updated = plateTemplateService.update(pt.getId(), pt);
        String actual = toJsonString(updated);
        assertJsonEquals(load("/PlateTemplateIT/updated.json"), actual, jsonScrubber);
    }

//    @Test
//    public void delete() throws Exception {
//        // create a new template
//        // delete it
//        // assert that we get a 404 if we try to get it
//    }

    private PlateTemplate createPlateTemplate() {
        return new PlateTemplate()
                .withName("plateTemplate-" + UUID.randomUUID().toString())
                .withDescription("my test plate")
                .withPlateType(plateType);
    }
}
