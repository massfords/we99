/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.harvard.we99.services;

import org.apache.commons.math3.fitting.PolynomialCurveFitter;
import org.apache.commons.math3.fitting.WeightedObservedPoints;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

/**
 * @author seansinnott
 */
public class MathServiceImpl implements MathService{
    
    private final PolynomialCurveFitter fitter = PolynomialCurveFitter.create(3);

    /**
     *
     * @param points Required to be n arrays of size 2.
     */
    @Override
    public double[] fitCurve(double[][] points) {
        
        if(points.length == 0){
            throw new WebApplicationException(Response.status(400).build());
        }
        
        // Create weighted points.
        WeightedObservedPoints obs = new WeightedObservedPoints();
        for(double[] point : points){
            if(point.length != 2){
                throw new WebApplicationException(Response.status(400).build());
            }
            obs.add(point[0], point[1]);
        }
        return fitter.fit(obs.toList());
        
    }
    
}
