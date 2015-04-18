package edu.harvard.we99.domain;

import edu.harvard.we99.domain.results.DoseResponseResult;
import edu.harvard.we99.domain.results.PlateResult;
import edu.harvard.we99.domain.results.ResultStatus;
import edu.harvard.we99.domain.results.StatusChange;
import edu.harvard.we99.services.ExperimentService;
import edu.harvard.we99.services.PlateTypeService;
import edu.harvard.we99.services.experiments.*;
import edu.harvard.we99.services.storage.CompoundStorage;
import edu.harvard.we99.test.EastCoastTimezoneRule;
import edu.harvard.we99.test.Scrubbers;
import org.junit.Rule;
import org.junit.Test;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

import static edu.harvard.we99.test.BaseFixture.assertJsonEquals;
import static edu.harvard.we99.test.BaseFixture.load;
import static edu.harvard.we99.test.BaseFixture.name;
import static edu.harvard.we99.test.BaseFixture.stream;
import static edu.harvard.we99.util.JacksonUtil.toJsonString;
import static org.junit.Assert.assertEquals;

/**
 * @author mford
 */
public class ResultServiceImplIT extends JpaSpringFixture {

    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Inject
    private ExperimentService experimentService;

    @Inject
    private CompoundStorage compoundStorage;

    @Inject
    private PlateTypeService plateTypeService;

    @Rule
    public EastCoastTimezoneRule eastCoastTimezoneRule = new EastCoastTimezoneRule();

    @Rule
    public AuthenticatedUserRule authenticatedUserRule =
            new AuthenticatedUserRule("we99.2015@gmail.com", this);

    @Test
    public void removeWellResult() throws Exception {
        Experiment exp = experimentService.create(
                new Experiment(name("xp")).setProtocol(new Protocol(name("proto")))
        );
        PlateType pt = plateTypeService.create(
                new PlateType()
                        .setName(name("PlateType"))
                        .setDim(new PlateDimension(5, 5))
                        .setManufacturer(name("Man")));
        PlatesResource pr = experimentService.getExperiment(exp.getId()).getPlates();
        Plate plate = pr.create(
                new Plate()
                        .setName(name("Plate"))
                        .withWells(makeWells(5, 5))
                        .setPlateType(pt)
        );


        PlateResource plates = pr.getPlates(plate.getId());
        PlateResult plateResult = plates.getPlateResult().uploadResults("csv", stream("/ResultServiceImplIT/results-single.csv"));

        Function<String, String> scrubber = Scrubbers.iso8601.andThen(Scrubbers.uuid).andThen(Scrubbers.pkey).andThen(Scrubbers.xpId);
        // assert the results
        assertJsonEquals(load("/ResultServiceImplIT/all-results.json"),
                toJsonString(plateResult), scrubber);

        // drop a well
        PlateResultResource resultResource = plates.getPlateResult();
        resultResource.updateStatus(
                new StatusChange(new Coordinate(0, 0), ResultStatus.EXCLUDED));

        // assert the results again
        PlateResult oneWellRemoved = resultResource.get();
        assertJsonEquals(load("/ResultServiceImplIT/one-removed.json"),
                toJsonString(oneWellRemoved), scrubber);

        // restore a well
        resultResource.updateStatus(
                new StatusChange(new Coordinate(0,0), ResultStatus.INCLUDED));

        // assert the results again
        PlateResult allWellsBack = resultResource.get();
        assertJsonEquals(load("/ResultServiceImplIT/all-results.json"),
                toJsonString(allWellsBack), scrubber);
    }

    @Test
    public void validatePlateMetrics() throws Exception {
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
                        .withWells(makeControlWells(5, 5))
                        .setPlateType(pt)
        );

        PlateResource plates = pr.getPlates(plate.getId());
        PlateResult plateResult = plates.getPlateResult().uploadResults("csv", stream("/ResultServiceImplIT/results-single.csv"));

        Function<String, String> scrubber = Scrubbers.iso8601.andThen(Scrubbers.uuid).andThen(Scrubbers.pkey).andThen(Scrubbers.xpId);
        // assert the results
        assertJsonEquals(load("/ResultServiceImplIT/all-results-with-metrics.json"),
                toJsonString(plateResult), scrubber);

    }


    @Test
    public void testDoseResponseResults() throws Exception {

        Compound c2 = new Compound().setName("SourPatchKids");
        compoundStorage.create(c2);
        Experiment exp = experimentService.create(
                new Experiment(name("xp"))
                        .setProtocol(new Protocol(name("protocol"))));
        PlateType pt = plateTypeService.create(
                new PlateType()
                        .setName(name("PlateType"))
                        .setDim(new PlateDimension(5, 5))
                        .setManufacturer(name("Man")));
        PlatesResource pr = experimentService.getExperiment(exp.getId()).getPlates();
        DoseResponseResource dr = experimentService.getExperiment(exp.getId()).getDoseResponses();
        Plate plate = pr.create(
                new Plate()
                        .setName(name("Plate"))
                        .withWells(makeDoseCompoundWells(5, 5, c2, c2))
                        .setPlateType(pt)
        );

        PlateResource plates = pr.getPlates(plate.getId());



        List<Plate> platesfordr = new ArrayList<>();
        platesfordr.add(plate);
        DoseResponseResult drr = dr.createForCompound(c2, platesfordr);
        DoseResponseResultResource d3 = dr.getDoseResponseResults(drr.getId());


        PlateResult plateResult = plates.getPlateResult().uploadResults("csv", stream("/ResultServiceImplIT/results-single.csv"));
        DoseResponseResult newResult = d3.addResponseValues();
        Function<String, String> scrubber = Scrubbers.iso8601.andThen(Scrubbers.uuid).andThen(Scrubbers.pkey);
        // assert the results
        /**
         * Removing temporarily until everyone gets python integration
         */
        //assertJsonEquals(load("/ResultServiceImplIT/all-doseresponse-results.json"),
        //        toJsonString(newResult), scrubber);

    }





    protected static Well[] makeWells(int rowCount, int colCount) {
        Set<Well> wells = new HashSet<>();

        for(int row=0; row< rowCount; row++) {
            for(int col=0; col< colCount; col++) {
                wells.add(
                        new Well(row, col)
                                .setType(WellType.EMPTY)
                );
            }
        }
        assertEquals(rowCount * colCount, wells.size());

        return wells.toArray(new Well[wells.size()]);
    }

    protected static Well[] makeCompoundWells(int rowCount, int colCount) {
        Set<Well> wells = new HashSet<>();

        for(int row=0; row< rowCount; row++) {
            for(int col=0; col< colCount; col++) {
                wells.add(
                        new Well(row, col)
                                .setType(WellType.COMP)
                );
            }
        }
        assertEquals(rowCount * colCount, wells.size());

        return wells.toArray(new Well[wells.size()]);
    }

    protected static Well[] makeControlWells(int rowCount, int colCount) {
        Set<Well> wells = new HashSet<>();

        for (int row = 0; row < rowCount; row++) {
            for (int col = 0; col < colCount; col++) {

                if (row % 2 == 0) {
                    wells.add(
                            new Well(row, col)
                                    .setType(WellType.POSITIVE)
                    );
                } else {

                    wells.add(
                            new Well(row, col)
                                    .setType(WellType.NEGATIVE)
                    );
                }
            }
        }
        assertEquals(rowCount * colCount, wells.size());

        return wells.toArray(new Well[wells.size()]);
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
