package edu.harvard.we99.domain.results.analysis;

import edu.harvard.we99.domain.results.DoseResponseResult;
import org.apache.cxf.helpers.IOUtils;

import java.io.File;
import java.io.InputStream;

import static edu.harvard.we99.util.JacksonUtil.fromString;
import static edu.harvard.we99.util.JacksonUtil.toJsonString;

/**
 * @author alan orcharton
 */
public class CurveFit {

    public static DoseResponseResult fitCurve(DoseResponseResult doseResponseResult) {
        DoseResponseResult response = null;

        try {

            String jsonDoseResponse = toJsonString(doseResponseResult);

            //String s = System.getProperty("we99");
            File f = new File(System.getProperty("we99"), "WEB-INF/classes/curveFitting.py");
            //src/main/resources/curveFitting.py

            ProcessBuilder pb = new ProcessBuilder("python", f.getPath(), jsonDoseResponse);
            Process p = pb.start();

            String error = IOUtils.toString(p.getErrorStream(), "UTF-8");
            if(error.isEmpty()){
                System.out.println(error);
            }
            InputStream in = p.getInputStream();


            String theString = IOUtils.toString(in,"UTF-8");
            response = fromString(theString,DoseResponseResult.class);


        } catch (Exception e){
            System.out.println("Found an Exception : " + e);
        }
        return response;
    }
}
