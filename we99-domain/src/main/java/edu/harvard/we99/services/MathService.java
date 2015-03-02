/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.harvard.we99.services;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Rest service which executes math and statistically operations.
 * 
 * @author seansinnott
 */
@Path("/math")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface MathService {
    
    /**
     * Given the points passed this service will computed the coefficients
     * of a polynomial function which determines a curve that fits a curve to 
     * the points. The input to this function is assumed to be a series of points,
     * 
     * [1.3, 3.4],
     * [3.4, 5.6],
     * etc.
     * 
     * The values returned are of a third degree polynomial which is defined as,
     * 
     * ( a * x^3 ) + ( b * x ^2 ) + ( c * x ) + d
     * 
     * The return value is of size four and are a, b, c, and d above.
     * 
     * @param points Required to be n arrays of size 2. 
     * @return The four values that make up the coefficients and constant value 
     *         in the polynomial curve function.
     * @statuscode 400 if you pass an empty array or one without 2 dimensional points
     */
    @POST
    @Path("fitCurve")
    public double[] fitCurve(double[][] points);
    
}
