package edu.harvard.we99.domain.results.analysis;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.harvard.we99.domain.CurveFitPoint;
import edu.harvard.we99.domain.ExperimentPoint;
import edu.harvard.we99.domain.Plate;
import edu.harvard.we99.domain.results.DoseResponseResult;
import static edu.harvard.we99.util.JacksonUtil.toJsonString;

import org.apache.cxf.helpers.IOUtils;

import static edu.harvard.we99.util.JacksonUtil.fromString;
import static edu.harvard.we99.util.JacksonUtil.toJsonString;

import java.io.*;
import java.net.URL;

/**
 * @author alan orcharton
 */
public class CurveFit {



    public static DoseResponseResult fitCurve(DoseResponseResult doseResponseResult) {
        DoseResponseResult response = null;


        try {

            String jsonDoseResponse = toJsonString(doseResponseResult);

            //String s = System.getProperty("we99");
            //File f = new File(System.getProperty("we99"), "WEB-INF/classes/curveFitting.py");

            ProcessBuilder pb = new ProcessBuilder("python", "src/main/resources/curveFitting.py", jsonDoseResponse);
            Process p = pb.start();

            InputStream in = p.getInputStream();


            StringWriter writer = new StringWriter();
            String theString = IOUtils.toString(in,"UTF-8");
            response = (DoseResponseResult)  fromString(theString,DoseResponseResult.class);


        } catch (Exception e){
            System.out.println("Found and Exception : " + e);
        }
        return response;
    }
}
