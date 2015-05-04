package edu.harvard.we99.services;

import edu.harvard.we99.domain.Label;
import edu.harvard.we99.domain.PlateMap;
import edu.harvard.we99.domain.WellMap;
import edu.harvard.we99.test.LogTestRule;
import edu.harvard.we99.test.Scrubbers;
import edu.harvard.we99.util.ClientFactory;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import java.net.URL;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static edu.harvard.we99.test.BaseFixture.assertJsonEquals;
import static edu.harvard.we99.test.BaseFixture.load;
import static edu.harvard.we99.util.JacksonUtil.toJsonString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Integration tests for the PlateMap Sample Data initialization
 *
 * @author SaZ
 */
public class PlateMapInitST {
    public static final String PMAP_NAME = "16x24";
    @Rule
    public LogTestRule logTestRule = new LogTestRule();
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
        List<PlateMap> plateMaps = plateMapService.listAll(null,null,null,null,null).getValues();
        assertTrue(plateMaps.size() > 0);
    }

    @Test
    public void plate5x5WasCreated() throws Exception {
        Optional result = findPlate(PMAP_NAME, 16, 24);
        assertTrue(result.isPresent());
    }

    @Test
    public void plate5x5HasLabelValues() throws Exception {
        PlateMap plateMap = (PlateMap)findPlate(PMAP_NAME, 16, 24).get();
        Set<String> labels = new HashSet<>();
        for (WellMap well : plateMap.getWells().values()) {
            labels.addAll(
                well.getLabels().stream()
                        .map(Label::getValue)
                        .collect(Collectors.toList())
            );
        }
        Set<String> expectedSet = new HashSet<>();
        expectedSet.addAll(Arrays.asList("A", "B", "C"));

        assertEquals(labels, expectedSet);
    }

    @Test
    public void plate5x5CreatesTheProperJson() throws Exception {
        PlateMap plateMap = (PlateMap)findPlate(PMAP_NAME, 16, 24).get();
        String actual = toJsonString(plateMap);
        assertJsonEquals(load("/PlateMapInitST/expected5x5.json"), actual, jsonScrubber);
    }

    private Optional findPlate(String name, int rows, int cols){
        List<PlateMap> plateMaps = plateMapService.listAll(null,null,null,null,null).getValues();
        return plateMaps.stream()
                .filter(plateMap -> plateMap.getName().equalsIgnoreCase(name) &&
                        plateMap.getDim().getRows() == rows &&
                        plateMap.getDim().getCols() == cols)
                .findAny();

    }


}
