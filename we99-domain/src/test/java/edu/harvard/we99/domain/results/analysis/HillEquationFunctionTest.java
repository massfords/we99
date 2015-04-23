package edu.harvard.we99.domain.results.analysis;

import edu.harvard.we99.domain.FitParameter;
import edu.harvard.we99.domain.ParameterStatus;
import org.assertj.core.util.Arrays;
import org.junit.Test;

import java.util.ArrayList;

import java.util.List;

import static org.junit.Assert.*;

/**
 * @author alan orchaton
 */
public class HillEquationFunctionTest {

    private static final DELTA = 1-E8''

    @Test
    public void testConvertingASinglePoint(){

        Double[] expectedResponses =  { -0.41975189,-0.12290589,0.28158758,0.83126429,1.57547217,
        2.57801833,3.9194994,5.69838781,8.02930894,11.03624747,
        14.83807468,19.52461662,25.12455061,31.57210553,38.68588719,
        46.17501514,53.6798003,60.83757249,67.34965135,73.024751,
        77.78792825,81.6611511,84.73043079,87.1132559 ,88.93386264,
        90.30801213,91.33565182,92.09886304,92.66278154,93.07786695};

        Double[] logX = { -10.0,-9.8,-9.6,-9.4,-9.2,-9.0,-8.8,-8.6,-8.4,-8.2,-8.0,-7.8, -7.6,-7.4,-7.2,-7.0,-6.8,-6.6,-6.4,-6.2,-6.0,-5.8,-5.6,-5.4,-5.2,-5.0,-4.8,-4.6,-4.4,-4.2};
        Double[] raw = {1e-10, 1.584893192461111e-10, 2.511886431509582e-10, 3.9810717055349694e-10, 6.309573444801942e-10, 1e-09, 1.584893192461111e-09, 2.511886431509582e-09, 3.981071705534969e-09,
                6.309573444801943e-09, 1e-08, 1.5848931924611143e-08, 2.511886431509582e-08, 3.981071705534969e-08, 6.30957344480193e-08, 1e-07, 1.584893192461114e-07, 2.5118864315095823e-07, 3.981071705534969e-07,
                6.30957344480193e-07, 1e-06, 1.584893192461114e-06, 2.5118864315095823e-06, 3.981071705534969e-06, 6.30957344480193e-06, 1e-05, 1.584893192461114e-05, 2.5118864315095822e-05,
                3.9810717055349695e-05, 6.309573444801929e-05};

        List<Double> points= new ArrayList<Double>(java.util.Arrays.asList(raw));


        FitParameter max = new FitParameter("Max",94.20960104195203, ParameterStatus.FLOAT);
        FitParameter min = new FitParameter("Min",-1.2252920335931403,ParameterStatus.FLOAT);
        FitParameter slope = new FitParameter("Slope",0.6880552631612014, ParameterStatus.FLOAT);
        FitParameter ec50 = new FitParameter("EC50",-6.991609838101542, ParameterStatus.FLOAT);

        HillEquationFunction hefunc = new HillEquationFunction(94.20960104195203,-1.2252920335931403,0.6880552631612014,-6.991609838101542 );
        List<Double> results = hefunc.apply(points);

        double[] actual = Arrays.toArray(results);
        double[] expected = new double[expectedResponses.length];

        for (int i =0; i < expected.length; i++){
             expected[i] = expectedResponses[i];
        }

        //assertArrayEquals(expected,actual,DELTA);

    }



}