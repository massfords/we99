package edu.harvard.we99.domain;

import edu.harvard.we99.domain.results.DoseResponseResult;
import edu.harvard.we99.domain.results.analysis.CurveFit;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

/**
 * @author alan orcharton
 */
public class CurveFitIT {


    @Test
    public void testResponseFromDoseResponseService() throws Exception {

        DoseResponseResult testDr = new DoseResponseResult().setId(100L).setFitEquation(FitEquation.HILLEQUATION);
        DoseResponseResult dr = CurveFit.fitCurve(testDr);

        //Temp removal until all get python installed
        //assertNotNull(dr.getId());

    }
}
