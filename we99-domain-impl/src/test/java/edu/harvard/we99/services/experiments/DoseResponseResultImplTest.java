package edu.harvard.we99.services.experiments;

import edu.harvard.we99.domain.*;
import edu.harvard.we99.domain.results.DoseResponseResult;
import edu.harvard.we99.services.ExperimentService;
import edu.harvard.we99.services.PlateTypeService;
import edu.harvard.we99.services.storage.CompoundStorage;
import edu.harvard.we99.services.storage.DoseResponseResultStorage;
import edu.harvard.we99.test.EastCoastTimezoneRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;

import javax.inject.Inject;

import java.util.*;

import static edu.harvard.we99.test.BaseFixture.assertJsonEquals;
import static edu.harvard.we99.test.BaseFixture.name;
import static org.junit.Assert.*;

import static edu.harvard.we99.test.BaseFixture.assertJsonEquals;
import static edu.harvard.we99.test.BaseFixture.load;
import static edu.harvard.we99.test.BaseFixture.name;
import static edu.harvard.we99.test.BaseFixture.stream;
import static edu.harvard.we99.util.JacksonUtil.toJsonString;

/**
 * @author alan orcharton
 */
public class DoseResponseResultImplTest extends JpaSpringFixture {

    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Inject
    private ExperimentService experimentService;

    @Inject
    private CompoundStorage compoundStorage;

    @Inject
    private DoseResponseResultStorage doseResponseStorage;

    @Inject
    private PlateTypeService plateTypeService;

    @Inject
    private DoseResponseResultResource doseResponseResultResource;

    @Rule
    public TestRule eastCoastTimezoneRule  = new EastCoastTimezoneRule();

    @Rule
    public AuthenticatedUserRule authenticatedUserRule =
            new AuthenticatedUserRule("we99.2015@gmail.com", this);


    @Test
    public void donothingTest() throws Exception {

    }
//    @Test
//    public void storeAndRetrieveADoseResponseResultTest() throws Exception {
//
//        Experiment exp = experimentService.create(
//                new Experiment(name("xp")).setProtocol(new Protocol(name("proto")))
//        );
//
//        DoseResponseResource dr = experimentService.getExperiment(exp.getId()).getDoseResponses();
//
//        Compound c1 = new Compound().setName("ChocolateFrog");
//        compoundStorage.create(c1);
//
//        PlateType pt = plateTypeService.create(
//                new PlateType()
//                        .setName(name("PlateType"))
//                        .setDim(new PlateDimension(5, 5))
//                        .setManufacturer(name("Man")));
//        PlatesResource pr = experimentService.getExperiment(exp.getId()).getPlates();
//        Plate plate = pr.create(
//                new Plate()
//                        .setName(name("Plate"))
//                        .withWells(makeDoseCompoundWells(5, 5, c1, c1))
//                        .setPlateType(pt)
//        );
//
//        List<Plate> plates = new ArrayList<>();
//        plates.add(plate);
//
//        //DoseResponseResult result = dr.createForCompound(c1, plates);
//
//        //assertNotNull(result.getId());
//
//
//    }



    protected static Well[] makeDoseCompoundWells(int rowCount, int colCount, Compound c1, Compound c2) {
        Set<Well> wells = new HashSet<>();



        Dose dose1 = new Dose()
                .setCompound(c1)
                .setAmount(new Amount(575, DoseUnit.MICROMOLAR));
        Set<Dose> set1 = new HashSet<>();
        set1.add(dose1);



        Dose dose2 = new Dose()
                .setCompound(c2)
                .setAmount(new Amount(220, DoseUnit.MICROMOLAR));
        Set<Dose> set2 = new HashSet<>();
        set2.add(dose2);

        for (int row = 0; row < rowCount; row++) {
            for (int col = 0; col < colCount; col++) {

                if (row % 2 == 0) {
                    wells.add(
                            new Well(row, col)
                                    .setType(WellType.COMP)
                                    .setContents(set1)


                    );
                } else {

                    wells.add(
                            new Well(row, col)
                                    .setType(WellType.COMP)
                                    .setContents(set1)

                    );
                }
            }
        }
        assertEquals(rowCount * colCount, wells.size());

        return wells.toArray(new Well[wells.size()]);
    }




}