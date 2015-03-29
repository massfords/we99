package edu.harvard.we99.domain.results.analysis;

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

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


import static org.junit.Assert.*;

/**
 * Tests to test Z Prime and Z Factor function returns valid values
 *
 * @author alan orcharton
 */


public class ZeeFactorFunctionTest {


    Predicate<WellResults> wellFilter1;
    Predicate<WellResults> wellFilter2;
    private static final double DELTA = 1e-3;

    /**
     * Test that an expected Z' value is returned for some sample positive
     * and negative controls.
     */
    @Test
    public void testZeePrimeFunction(){

        Map<Coordinate,WellType> wellTypes = new HashMap<>();

        double[] positive = {0.124,0.120,0.120};
        double[] negative = {0.65,0.54, 0.60};
        List<WellResults> positiveControlResults = makeWellResults(positive,1);
        for(WellResults wr : positiveControlResults){
            wellTypes.put(wr.getCoordinate(),WellType.POSITIVE);
        }
        List<WellResults> negativeControlResults = makeWellResults(negative,2);
        for(WellResults wr : negativeControlResults){
            wellTypes.put(wr.getCoordinate(),WellType.NEGATIVE);
        }

        List<WellResults> allResults = new ArrayList<>();
        allResults.addAll(positiveControlResults);
        allResults.addAll(negativeControlResults);


        wellFilter1 = wellResults -> wellTypes.get(wellResults.getCoordinate()) == WellType.POSITIVE;
        wellFilter2 = wellResults -> wellTypes.get(wellResults.getCoordinate()) == WellType.NEGATIVE;

        ZeeFactorFunction zf = new ZeeFactorFunction("testLabel",wellFilter1,wellFilter2);
        double res = zf.apply(allResults);

        assertEquals(0.7042828263355972, res,DELTA);
    }

    /**
     * If the positive and negative controls happen to have the same Mean then
     * The Z' value should be invalid and should return null
     */
    @Test
    public void testZeePrimeFunctionWithSameMean() {


        Map<Coordinate,WellType> wellTypes = new HashMap<>();

        double[] positive = {4.0,8.0};
        double[] negative = {10.0,2.0};
        List<WellResults> positiveControlResults = makeWellResults(positive,1);
        for(WellResults wr : positiveControlResults){
            wellTypes.put(wr.getCoordinate(),WellType.POSITIVE);
        }
        List<WellResults> negativeControlResults = makeWellResults(negative,2);
        for(WellResults wr : negativeControlResults){
            wellTypes.put(wr.getCoordinate(),WellType.NEGATIVE);
        }

        List<WellResults> allResults = new ArrayList<>();
        allResults.addAll(positiveControlResults);
        allResults.addAll(negativeControlResults);

        wellFilter1 = wellResults -> wellTypes.get(wellResults.getCoordinate()) == WellType.POSITIVE;
        wellFilter2 = wellResults -> wellTypes.get(wellResults.getCoordinate()) == WellType.NEGATIVE;

        ZeeFactorFunction zf = new ZeeFactorFunction("testLabel",wellFilter1, wellFilter2);
        Double res = zf.apply(allResults);

        assertNull(res);

    }

    /**
     * Helper method to make a list of mock WellResult objects
     * @param values - sample result values
     * @param row - row of the plate (used for a coordinate)
     * @return A list of mock WellResults objects
     */
    private static List<WellResults> makeWellResults(double[] values, int row){

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