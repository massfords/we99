package edu.harvard.we99.services;

import edu.harvard.we99.domain.DoseUnit;
import edu.harvard.we99.domain.Experiment;
import edu.harvard.we99.domain.PlateDimension;
import edu.harvard.we99.domain.PlateType;
import edu.harvard.we99.domain.Protocol;
import edu.harvard.we99.domain.WellType;
import edu.harvard.we99.domain.lists.Plates;
import edu.harvard.we99.test.LogTestRule;
import edu.harvard.we99.test.Scrubbers;
import edu.harvard.we99.util.ClientFactory;
import org.apache.commons.io.IOUtils;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import static edu.harvard.we99.test.BaseFixture.assertJsonEquals;
import static edu.harvard.we99.test.BaseFixture.load;
import static edu.harvard.we99.test.BaseFixture.name;
import static edu.harvard.we99.util.JacksonUtil.toJsonString;
import static org.assertj.core.util.Arrays.array;

/**
 * @author markford
 */
@RunWith(Parameterized.class)
public class FullMontyST {
    @Rule
    public LogTestRule logTestRule = new LogTestRule();

    private static final String pattern = "%d, %d, %s, , , %s, %f, %s, %f";
    private static ExperimentService es;
    private static PlateTypeService pts;
    private final Random r = new Random();

    private Experiment xp;

    @BeforeClass
    public static void init() throws Exception {
        URL url = new URL(WebAppIT.WE99_URL);
        ClientFactory cf = new ClientFactory(url, WebAppIT.WE99_EMAIL, WebAppIT.WE99_PW);

        es = cf.create(ExperimentService.class);
        pts = cf.create(PlateTypeService.class);
    }

    @AfterClass
    public static void tearDown() throws Exception {
        es = null;
        pts = null;
    }

    @Parameterized.Parameters
    public static List<Object[]> params() throws Exception {
        List<Object[]> params = new ArrayList<>();

        params.add(array(PlateClientStrategy.json));
        params.add(array(PlateClientStrategy.text));

        return params;
    }

    @Before
    public void setUp() throws Exception {
        xp = es.create(
                new Experiment(name("Experiment")).setProtocol(new Protocol(name("p")))
        );
    }

    public enum PlateClientStrategy {
        json {
            @Override
            public void run(PlatesClient pc, Experiment experiment, PlateType plateType, InputStream csv) throws Exception {
                pc.fullMonty(experiment, plateType, csv);
            }
        }, text {
            @Override
            public void run(PlatesClient pc, Experiment experiment, PlateType plateType, InputStream csv) throws Exception {
                pc.stringMonty(experiment, plateType, csv);
            }
        };

        public abstract void run(PlatesClient pc, Experiment experiment, PlateType plateType, InputStream csv) throws Exception;
    }


    private final PlateClientStrategy strategy;

    public FullMontyST(PlateClientStrategy strategy) {
        this.strategy = strategy;
    }


    @Test
    public void test() throws Exception {

        // make a CSV for our tests (too big to do manually)
        List<String> compounds = allCompounds();

        List<String> lines = new ArrayList<>();

        while(compounds.size()>=16) {
            // add all compounds above
            for(int row=0; row<14; row++) {
                String c = compounds.remove(0);
                for(int col=0; col<24; col++) {
                    lines.add(String.format(pattern, row, col,
                            WellType.COMP.name(), c, (1.0f) * (row + 1),
                            DoseUnit.MICROMOLAR, r.nextDouble()));
                }
            }
            addControls(lines, r);
        }

        StringBuilder sb = new StringBuilder();
        lines.stream().forEach(s-> sb.append(s).append("\n"));

//        System.out.println(sb);

        // upload the AIRF full monty file
        PlateType pt = pts.create(
                new PlateType()
                        .setName(name("pt")).setManufacturer("Foo Inc")
                        .setDim(new PlateDimension(16, 24)));
        PlatesClient pc = new PlatesClient(new URL(WebAppIT.WE99_URL), WebAppIT.WE99_EMAIL, WebAppIT.WE99_PW);
        strategy.run(pc, xp, pt, new ByteArrayInputStream(sb.toString().getBytes("UTF-8")));

        Plates plates = es.getExperiment(xp.getId()).getPlates().list(0, 100, null);
        assertJsonEquals(
                load("/FullMontyST/plates.json"), toJsonString(plates),
                Scrubbers.pkey.andThen(Scrubbers.iso8601).andThen(Scrubbers.uuid)
        );
    }

    private List<String> allCompounds() throws Exception {
        String all = IOUtils.toString(getClass().getResourceAsStream("/FullMontyST/all-compounds.txt"));
        String[] split = all.split("\n");
        return Arrays.asList(split).stream().collect(Collectors.toList());
    }

    private void addControls(List<String> lines, Random r) {
        for(int col=0; col<24; col++) {
            lines.add(String.format(pattern, 14, col, WellType.POSITIVE.name(), "pos-comp1", 1.0f, DoseUnit.MICROMOLAR, r.nextDouble()));
        }
        for(int col=0; col<24; col++) {
            lines.add(String.format(pattern, 15, col, WellType.NEGATIVE.name(), "neg-comp1", 1.0f, DoseUnit.MICROMOLAR, r.nextDouble()));
        }
    }
}
