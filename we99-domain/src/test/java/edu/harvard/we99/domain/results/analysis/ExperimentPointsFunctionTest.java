package edu.harvard.we99.domain.results.analysis;

import edu.harvard.we99.domain.*;
import edu.harvard.we99.domain.results.DoseResponseResult;
import edu.harvard.we99.domain.results.Sample;
import edu.harvard.we99.domain.results.WellResults;
import org.junit.Test;

import java.util.*;

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
        doseList.add(new Dose(c,new Amount(3.33, DoseUnit.MICROMOLAR)).setWell(w1).setId(300L));
        doseList.add(new Dose(c,new Amount(5.55, DoseUnit.MICROMOLAR)).setWell(w3).setId(500L));

        List<Dose> doseList2 = new ArrayList<>();
        doseList2.add(new Dose(c, new Amount(4.44, DoseUnit.MICROMOLAR)).setWell(w2).setId(400L));

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
        WellResults wr1 = new WellResults().setSamples(samples1).setCoordinate(co1);
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

}