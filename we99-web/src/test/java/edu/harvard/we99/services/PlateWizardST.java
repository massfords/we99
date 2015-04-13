package edu.harvard.we99.services;

import edu.harvard.we99.domain.Amount;
import edu.harvard.we99.domain.Compound;
import edu.harvard.we99.domain.Dose;
import edu.harvard.we99.domain.DoseUnit;
import edu.harvard.we99.domain.Experiment;
import edu.harvard.we99.domain.Plate;
import edu.harvard.we99.domain.PlateDimension;
import edu.harvard.we99.domain.PlateMap;
import edu.harvard.we99.domain.PlateMapMergeInfo;
import edu.harvard.we99.domain.PlateType;
import edu.harvard.we99.domain.Protocol;
import edu.harvard.we99.domain.WellType;
import edu.harvard.we99.domain.lists.PlateMaps;
import edu.harvard.we99.test.LogTestRule;
import edu.harvard.we99.test.Scrubbers;
import edu.harvard.we99.util.ClientFactory;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import static edu.harvard.we99.test.BaseFixture.assertJsonEquals;
import static edu.harvard.we99.test.BaseFixture.load;
import static edu.harvard.we99.test.BaseFixture.name;
import static edu.harvard.we99.util.JacksonUtil.toJsonString;
import static org.junit.Assert.fail;

/**
 * @author markford
 */
public class PlateWizardST {
    @Rule
    public LogTestRule logTestRule = new LogTestRule();
    private static ExperimentService es;
    private static PlateTypeService pts;
    private static PlateMapService plateMapService;

    @BeforeClass
    public static void init() throws Exception {
        URL url = new URL(WebAppIT.WE99_URL);
        ClientFactory cf = new ClientFactory(url, WebAppIT.WE99_EMAIL, WebAppIT.WE99_PW);

        es = cf.create(ExperimentService.class);
        pts = cf.create(PlateTypeService.class);
        plateMapService = cf.create(PlateMapService.class);
    }

    @AfterClass
    public static void tearDown() throws Exception {
        es = null;
    }

    @Test
    public void test() throws Exception {
        // ---------------------------------------------------------------------
        // First, create a plate type. There are likely ones in the system already
        // but let's make sure there's one that suits our needs
        // ---------------------------------------------------------------------
        PlateType plateType = pts.create(
                new PlateType()
                        .setName(name("type"))
                        .setManufacturer("Foo Inc")
                        .setDim(new PlateDimension(10, 10))
        );

        // ---------------------------------------------------------------------
        // Next we upload plate map. Again, there may be items already in the system
        // but we'll load one here to be sure. We also give it a unique name at
        // the time we load it. The name() function below will concat a UUID to
        // the value provided.
        // ---------------------------------------------------------------------
        String plateMapName = name("pmap");
        PlateMapClientFixture.upload("/PlateWizardST/plateMap5x5.csv", plateMapName);

        // ---------------------------------------------------------------------
        // Now we create experiment. You cannot create any plates outside
        // of an experiment.
        // ---------------------------------------------------------------------
        Experiment experiment = es.create(
                new Experiment((name("exp"))).setProtocol(new Protocol(name("p")))
        );

        // ---------------------------------------------------------------------
        // Simulate the behavior of the user at the Plate Wizard. The first step
        // involves selecting a plate type. Let's assume that they're using the
        // plate type we just added in the step above.
        //
        // Given the plate type above, get its dimension and find all suitable
        // plate maps. We'll walk the results here to be sure that we have the
        // one we just added above.
        // ---------------------------------------------------------------------
        PlateDimension dim = plateType.getDim();
        PlateMaps plateMaps = plateMapService.listAll(null, null, dim.getRows(), dim.getCols(), null);
        PlateMap found = null;
        for(PlateMap plateMap : plateMaps.getValues()) {
            if (plateMap.getName().equals(plateMapName)) {
                found = plateMap;
                break;
            }
        }
        if (found == null) {
            fail("failed to find the plate map we just added!");
        }

        // ---------------------------------------------------------------------
        // Make a call to the PlateMapService to get a PlateMapMergeInfo object
        // that summarizes what the map looks like.
        // - How many wells have the same contents label value?
        // - What is the well type for each of these labeled wells?
        // ---------------------------------------------------------------------
        PlateMapMergeInfo mergeInfo = plateMapService.prepare(found.getId(), plateType);

        // ---------------------------------------------------------------------
        // The UI will render the mergeInfo in a table of some sorts and prompt
        // the user to provide the missing information in order to make a new
        // plate for Dose Response Curves.
        //
        // Missing info at this point is the following:
        // - what is the name of the plate we want to create?
        // - given a label A, B, C, etc, what Dose should we use?
        // - note: a Dose = Compound + Amount
        // - should a dilution factor be applied to the well? If so, this dilution
        //   factor will dilute each well of the same label/Dose after the first
        //   by the given factor each time.
        // - should multiple copies of wells get replicated? Meaning, if there are
        //   12 copies of compound A, the user might want 2 copies of each Dose
        // ---------------------------------------------------------------------
        mergeInfo.setPlateName(name("my plate name"));

        Map<String,Compound> mappings = new HashMap<>();
        mappings.put("A", new Compound("Lactic acid"));
        mappings.put("B", new Compound("Nicotine"));

        // set the compounds in place
        mergeInfo.getMappings().values()
                .stream()
                .filter(wlm -> wlm.getWellType() != WellType.EMPTY)
                .forEach(wlm -> {
                    wlm.setReplicates(2);
                    wlm.setDilutionFactor(5d);
                    wlm.setDose(new Dose(mappings.get(wlm.getLabel()),
                            new Amount(100, DoseUnit.MICROMOLAR)));
                });

        String request = toJsonString(mergeInfo);
//        System.out.println(request);
        assertJsonEquals(load("/PlateWizardST/request.json"), request,
                Scrubbers.uuid.andThen(Scrubbers.pkey));


        // ---------------------------------------------------------------------
        // At this point the merge info is fully populated and we're ready to
        // create a new plate. There's a new service on the PlatesResource that
        // accepts the merge info. The response of this call is a
        // ---------------------------------------------------------------------
        Plate plate = es.getExperiment(experiment.getId()).getPlates().create(mergeInfo);
        assertJsonEquals(load("/PlateWizardST/expected-plate.json"),
                toJsonString(plate), Scrubbers.pkey.andThen(
                        Scrubbers.uuid.andThen(Scrubbers.iso8601).andThen(Scrubbers.xpId)));
    }
}
