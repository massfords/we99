package edu.harvard.we99.domain.results.analysis;

import edu.harvard.we99.domain.ExperimentPoint;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by HUID 70786729 on 4/22/15.
 */
public class CurveFitParametersFunctionTest {

    @Test
    public void testGettingFitParameters() {
        Double[] wellDoses = {0.00003,9.49E-06,3.00E-06,9.51E-07,3.01E-07,
                9.52E-08,3.01E-08,9.53E-09,3.02E-09,9.55E-10,3.02E-10};

        Double[] responses = {71.79121535497015,47.08544254001027,21.527656402541446,5.3664098321291975,-3.0160770688737975,-5.584313546129757,
                -5.843053788241366,-2.502081301716058,-3.7339287843618965,0.35364433344168644,-0.16819204711337463};

        List<ExperimentPoint> forFitting = makeExperimentPoints(wellDoses,responses);

        CurveFitParametersFunction cfp = new CurveFitParametersFunction();
        cfp.apply(forFitting);



    }


    private static List<ExperimentPoint> makeExperimentPoints(Double[] xpoints, Double[] ypoints){

        assert xpoints.length == ypoints.length;

        int numPoints = xpoints.length;

        List<ExperimentPoint> eps = new ArrayList<>();
        for(int i=0; i < numPoints; i++){
            ExperimentPoint newpoint = new ExperimentPoint();
            newpoint.setX(xpoints[i]);
            newpoint.setY(ypoints[i]);
            eps.add(newpoint);
        }
        return eps;
    }
}