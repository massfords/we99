package edu.harvard.we99.domain.results.analysis;

import edu.harvard.we99.domain.*;
import edu.harvard.we99.domain.results.*;
import org.junit.Test;

import java.util.*;

import static edu.harvard.we99.test.BaseFixture.name;
import static org.junit.Assert.*;

/**
 * @alan orcharton
 */
public class ExperimentPointsFunctionTest {

    @Test
    public void testFuctionWithSinglePlateResults() {

        Coordinate co1 = new Coordinate(1,0);
        Coordinate co2 = new Coordinate(2,0);
        Coordinate co3 = new Coordinate(3,0);

        Well w1 = new Well().setCoordinate(co1);
        Well w2 = new Well().setCoordinate(co2);
        Well w3 = new Well().setCoordinate(co3);

        Compound c = new Compound("Sugar");
        List<Dose> doseList = new ArrayList<>();
        doseList.add(new Dose(c,new Amount(3.33, DoseUnit.MICROMOLAR)).setWell(w1).setId(300L).setPlateId(1L));
        doseList.add(new Dose(c,new Amount(5.55, DoseUnit.MICROMOLAR)).setWell(w3).setId(500L).setPlateId(1L));

        List<Dose> doseList2 = new ArrayList<>();
        doseList2.add(new Dose(c, new Amount(4.44, DoseUnit.MICROMOLAR)).setWell(w2).setId(400L).setPlateId(2L));

        List<Dose> doseList3 = new ArrayList<>();
        doseList3.addAll(doseList);
        doseList3.addAll(doseList2);

        DoseResponseResult dr = new DoseResponseResult().setCompound(c).setDoses(doseList3);

        ExperimentPoint ep1 = new ExperimentPoint(1L,300L);
        ExperimentPoint ep2 = new ExperimentPoint(2L,400L);
        ExperimentPoint ep3 = new ExperimentPoint(1L,500L);

        List<ExperimentPoint> loadPoints = new ArrayList<>();
        loadPoints.add(ep1);
        loadPoints.add(ep2);
        loadPoints.add(ep3);
        dr.setExperimentPoints(loadPoints);
        Sample s1 = new Sample(100.00);
        Sample s2 = new Sample(200.00);
        Sample s3 = new Sample(300.00);

        List<Sample> samples1 = new ArrayList<>();
        samples1.add(s1);
        List<Sample> samples2 = new ArrayList<>();
        samples2.add(s2);
        List<Sample> samples3 = new ArrayList<>();
        samples3.add(s3);

        List<WellResults> wellresults = new ArrayList<>();
        List<WellResults> wellresults2 = new ArrayList<>();
        WellResults wr1 = new WellResults().setSamples(samples1).setCoordinate(co1).setResultStatus(ResultStatus.EXCLUDED);
        WellResults wr2 = new WellResults().setSamples(samples2).setCoordinate(co2);
        WellResults wr3 = new WellResults().setSamples(samples3).setCoordinate(co3);

        wellresults.add(wr1);
        wellresults2.add(wr2);
        wellresults.add(wr3);



        Map<Long,List<WellResults>> plateToResults = new HashMap<>();
        plateToResults.put(1L, wellresults);
        plateToResults.put(2L, wellresults2);

        Set<Long> plateid = new HashSet<>();
        plateid.add(1L);
        plateid.add(2L);

        ExperimentPointsFunction epf = new ExperimentPointsFunction(dr,plateid);
        List<ExperimentPoint> newPoints = epf.apply(plateToResults);


    }

    @Test
    public void testExperimentPointsCreationWithPlateData() {

        Double[] positive = {138.478,144.927,144.34,145.219,145.654,145.924,144.77,140.504,
                138.814,142.404,144.539,145.242,142.971,139.508,145.795,141.695};

        Double[] negative = {30.046,29.32,31.546,26.173,29.214,25.645,27.317,30.404,
                30.939,27.316,25.647,27.482,25.806,30.73,29.419,27.189};

        Double[] wellResults = {110.794,82.435,53.098,34.547,24.925,21.977,21.68,25.515,24.101,
                28.793,28.194};

        Double[] wellDoses = {0.00003,9.49E-06,3.00E-06,9.51E-07,3.01E-07,
                9.52E-08,3.01E-08,9.53E-09,3.02E-09,9.55E-10,3.02E-10};

        Double[] responses = {71.79121535497015,47.08544254001027,21.527656402541446,5.3664098321291975,-3.0160770688737975,-5.584313546129757,
                -5.843053788241366,-2.502081301716058,-3.7339287843618965,0.35364433344168644,-0.16819204711337463};


        Compound c = new Compound("Sugar");
//        PlateType pt =  new PlateType()
//                .setName(name("PlateType"))
//                .setDim(new PlateDimension(16, 24))
//                .setManufacturer(name("Man"));
//
//        Plate testPlate = new Plate()
//                .setName(name("Plate"))
//                .withWells(makeDoseCompoundWells(5,5,c,c))
//                .setPlateType(pt);
//
//        PlateResult = new PlateResult().setWellResults()

        Well[] wells = makeDoseCompoundWells(16,24,c,wellDoses);
        System.out.println("Hello");


    }


    private static Well[] makeDoseCompoundWells(int rowCount, int colCount, Compound c1, Double[] doses) {
        Set<Well> wells = new HashSet<>();

        Queue<Dose> doseQueue = new LinkedList<>();
        for (Double d: doses){
            Dose aDose = new Dose();
            aDose.setAmount(new Amount(d, DoseUnit.MICROMOLAR));
            aDose.setCompound(c1);
            doseQueue.add(aDose);

        }


        for (int row = 0; row < rowCount; row++) {
            for (int col = 0; col < colCount; col++) {


                if(col == 0){
                    wells.add(
                            new Well(row,col)
                                    .setType(WellType.POSITIVE)
                    );
                }
                else if (col == 1){
                    if(!doseQueue.isEmpty()) {
                        Dose d = (Dose) doseQueue.remove();
                        Set<Dose> ds = new HashSet<>();
                        ds.add(d);
                        wells.add(
                                new Well(row, col)
                                        .setType(WellType.COMP)
                                        .setContents(ds)

                        );
                    } else {
                        wells.add(
                                new Well(row, col)
                                        .setType(WellType.EMPTY)
                        );

                    }
                } else if(col == 23){
                    wells.add(
                            new Well(row, col)
                                    .setType(WellType.NEGATIVE)
                    );

                } else {
                    wells.add(
                            new Well(row, col)
                                    .setType(WellType.EMPTY)
                    );

                }
            }
        }
        assertEquals(rowCount * colCount, wells.size());

        return wells.toArray(new Well[wells.size()]);
    }

}