package edu.harvard.we99.services;

import edu.harvard.we99.test.LogTestRule;
import edu.harvard.we99.util.ClientFactory;
import org.apache.commons.math3.analysis.polynomials.PolynomialFunction;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.net.URL;
import java.util.Random;

import static org.junit.Assert.assertArrayEquals;

/**
 * We're not looking at recreating unit tests for commons-math. The tests here
 * are simply to confirm the services can be invoked.
 *
 * @author mford
 */
public class MathServiceST {
    @Rule
    public LogTestRule logTestRule = new LogTestRule();
    private MathService ms;

    @Before
    public void init() throws Exception {

        ClientFactory cf = new ClientFactory(new URL(WebAppIT.WE99_URL),
                WebAppIT.WE99_EMAIL, WebAppIT.WE99_PW);

        ms = cf.create(MathService.class);
    }


    @Test
    public void test() throws Exception {

        Random randomizer = new Random(1234);
        PolynomialFunction p = buildRandomPolynomial(randomizer);

        double[][] points = new double[4][2];
        for (int i = 0; i < points.length; ++i) {
            points[i] = new double[] {i, p.value(i)};
        }

        double[] curve = ms.fitCurve(points);

        double[] expected = {
                0.14115907833078006,
                0.4346588858045115,
                1.1384720144986946,
                -0.1329746864592334
        };
        assertArrayEquals(expected, curve, .000001);
    }

    private PolynomialFunction buildRandomPolynomial(Random randomizer) {
        final double[] coefficients = new double[3 + 1];
        for (int i = 0; i <= 3; ++i) {
            coefficients[i] = randomizer.nextGaussian();
        }
        return new PolynomialFunction(coefficients);
    }
}
