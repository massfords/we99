package edu.harvard.we99.domain;

import edu.harvard.we99.domain.results.DoseResponseResult;
import edu.harvard.we99.services.ExperimentService;
import edu.harvard.we99.services.PlateTypeService;
import edu.harvard.we99.services.experiments.DoseResponseResource;
import edu.harvard.we99.services.experiments.PlatesResource;
import edu.harvard.we99.services.storage.CompoundStorage;
import edu.harvard.we99.services.storage.DoseResponseResultStorage;
import edu.harvard.we99.services.storage.entities.DoseResponseResultEntity;
import edu.harvard.we99.test.EastCoastTimezoneRule;
import org.junit.Rule;
import org.junit.Test;

import javax.inject.Inject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static edu.harvard.we99.test.BaseFixture.name;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author alan orcharton
 */
public class JpaDoseResponseIPT extends JpaSpringFixture {

    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Inject
    private ExperimentService experimentService;

    @Inject
    private CompoundStorage compoundStorage;

    @Inject
    private PlateTypeService plateTypeService;

    @Inject
    private DoseResponseResultStorage doseResponseResultStorage;

    @Rule
    public EastCoastTimezoneRule eastCoastTimezoneRule = new EastCoastTimezoneRule();

    @Rule
    public AuthenticatedUserRule authenticatedUserRule =
            new AuthenticatedUserRule("we99.2015@gmail.com", this);


    @Test
    public void createDoseResponseWithFitParameter() throws Exception{

        Compound c2 = new Compound().setName("Smarties");
        Compound newCompound = compoundStorage.create(c2);

        Experiment exp = experimentService.create(
                new Experiment(name("xp"))
                        .setProtocol(new Protocol(name("protocol"))));

        FitParameter fp = new FitParameter("top",130.0,ParameterStatus.FLOAT);

        DoseResponseResult drr = new DoseResponseResult().setExperiment(exp).setCompound(newCompound);

        DoseResponseResult storedDrr = doseResponseResultStorage.create(drr);
        DoseResponseResult withParam = doseResponseResultStorage.addFitParameter(storedDrr.getId(), fp);

        assertNotNull(withParam.getFitEquation());
        assertNotNull(withParam.getFitParameterMap());


    }

    @Test
    public void validateDoseResponseWithFitParameter() throws Exception{

        Compound c2 = new Compound().setName("Smarties");
        Experiment exp = experimentService.create(
                new Experiment(name("xp"))
                        .setProtocol(new Protocol(name("protocol"))));
        PlateType pt = plateTypeService.create(
                new PlateType()
                        .setName(name("PlateType"))
                        .setDim(new PlateDimension(5, 5))
                        .setManufacturer(name("Man")));
        PlatesResource pr = experimentService.getExperiment(exp.getId()).getPlates();
        Plate plate = pr.create(
                new Plate()
                        .setName(name("Plate"))
                        .withWells(makeDoseCompoundWells(5, 5, c2, c2))
                        .setPlateType(pt)
        );

        List<Plate> platesfordr = new ArrayList<>();
        DoseResponseResource drResource = experimentService.getExperiment(exp.getId()).getDoseResponses();
        drResource.create();



    }


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
