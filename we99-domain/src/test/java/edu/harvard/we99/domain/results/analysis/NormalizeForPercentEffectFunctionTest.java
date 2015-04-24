package edu.harvard.we99.domain.results.analysis;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import edu.harvard.we99.domain.Coordinate;
import edu.harvard.we99.domain.WellType;
import edu.harvard.we99.domain.results.Sample;
import edu.harvard.we99.domain.results.WellResults;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;


/**
 *@author alan orcharton
 */
public class NormalizeForPercentEffectFunctionTest {
    Predicate<WellResults> wellFilter1;
    Predicate<WellResults> wellFilter2;
    Predicate<WellResults> wellFilter3;
    private static final double DELTA = 1e-3;



    @Test
    public void testCompoundNormalization(){

        Map<Coordinate,WellType> wellTypes = new HashMap<>();

        Double[] positive = {138.478,144.927,144.34,145.219,145.654,145.924,144.77,140.504,
                138.814,142.404,144.539,145.242,142.971,139.508,145.795,141.695};

        Double[] negative = {30.046,29.32,31.546,26.173,29.214,25.645,27.317,30.404,
                30.939,27.316,25.647,27.482,25.806,30.73,29.419,27.189};

        Double[] wellResults = {110.794,82.435,53.098,34.547,24.925,21.977,21.68,25.515,24.101,
                28.793,28.194};

        List<WellResults> positiveControlResults = makeWellResults(positive,1);
        for(WellResults wr : positiveControlResults){
            wellTypes.put(wr.getCoordinate(),WellType.POSITIVE);
        }
        List<WellResults> negativeControlResults = makeWellResults(negative,2);
        for(WellResults wr : negativeControlResults){
            wellTypes.put(wr.getCoordinate(),WellType.NEGATIVE);
        }
        List<WellResults> compundResults = makeWellResults(wellResults,3);
        for(WellResults wr : compundResults){
            wellTypes.put(wr.getCoordinate(),WellType.COMP);
        }

        List<WellResults> allResults = new ArrayList<>();
        allResults.addAll(positiveControlResults);
        allResults.addAll(negativeControlResults);
        allResults.addAll(compundResults);
        wellFilter1 = wellRes -> wellTypes.get(wellRes.getCoordinate()) == WellType.POSITIVE;
        wellFilter2 = wellRes -> wellTypes.get(wellRes.getCoordinate()) == WellType.NEGATIVE;
        wellFilter3 = wellRes -> wellTypes.get(wellRes.getCoordinate()) == WellType.COMP;

        NormalizeForPercentEffectFunction nf = new NormalizeForPercentEffectFunction("testlabel",
                wellFilter1, wellFilter2, wellFilter3);

        List<WellResults> normalized = nf.apply(allResults);
        double[] normResults = new double[normalized.size()];
        int i =0;
        for(WellResults w : normalized){
           normResults[i++] = w.getByLabel("testresults").getNormalized();
        }

    }


    /**
     * Helper method to make a list of mock WellResult objects
     * @param values - sample result values
     * @param row - row of the plate (used for a coordinate)
     * @return A list of mock WellResults objects
     */
    private static List<WellResults> makeWellResults(Double[] values, int row){

        List<WellResults> wrList = new ArrayList<>();

        for(int i = 0; i < values.length; i++){
            WellResults wr = mock(WellResults.class);
            Sample s = mock(Sample.class);
            when(s.getValue()).thenReturn(values[i]);
            when(wr.getByLabel(anyString())).thenReturn(s);
            when(wr.getCoordinate()).thenReturn(new Coordinate(row,i));

            wrList.add(wr);

        }
        return wrList;
    }



}