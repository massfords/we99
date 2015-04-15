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

import java.io.InputStream;
import java.io.StringWriter;

/**
 * @author alan orcharton
 */
public class CurveFit {

    public static DoseResponseResult fitCurve(DoseResponseResult doseResponseResult) {
        DoseResponseResult response = null;



        try {

            String jsonDoseResponse = toJsonString(doseResponseResult);

            String json = "{\"x\" : 5 }";
            String json3 = "{\n" +
                    "  \"doseWells\" : [ ],\n" +
                    "  \"experimentPoints\" : [ ],\n" +
                    "  \"curveFitPoints\" : [ {\n" +
                    "    \"x\" : 5.0,\n" +
                    "    \"y\" : 6.0,\n" +
                    "    \"sequenceNumber\" : 1\n" +
                    "  }, {\n" +
                    "    \"x\" : 5.0,\n" +
                    "    \"y\" : 6.0,\n" +
                    "    \"sequenceNumber\" : 1\n" +
                    "  }, {\n" +
                    "    \"x\" : 5.0,\n" +
                    "    \"y\" : 6.0,\n" +
                    "    \"sequenceNumber\" : 1\n" +
                    "  } ],\n" +
                    "  \"fitEquation\" : \"HILLEQUATION\",\n" +
                    "  \"fitParameterMap\" : { }\n" +
                    "}";
            String json2 = "{\"curveFitPoints\": [{\"y\": -2.9667803082152915, \"x\": -10.0, \"sequenceNumber\": 0}, {\"y\": -2.7946475022487647, \"x\": -9.8, \"sequenceNumber\": 1}, {\"y\": -2.546932084721604, \"x\": -9.600000000000001, \"sequenceNumber\": 2}, {\"y\": -2.191138713075702, \"x\": -9.400000000000002, \"sequenceNumber\": 3}, {\"y\": -1.6815378878378766, \"x\": -9.200000000000003, \"sequenceNumber\": 4}, {\"y\": -0.9545507691536557, \"x\": -9.000000000000004, \"sequenceNumber\": 5}, {\"y\": 0.0766644892373165, \"x\": -8.800000000000004, \"sequenceNumber\": 6}, {\"y\": 1.527662540960364, \"x\": -8.600000000000005, \"sequenceNumber\": 7}, {\"y\": 3.5463097561312225, \"x\": -8.400000000000006, \"sequenceNumber\": 8}, {\"y\": 6.310824560340265, \"x\": -8.200000000000006, \"sequenceNumber\": 9}, {\"y\": 10.016298678373417, \"x\": -8.000000000000007, \"sequenceNumber\": 10}, {\"y\": 14.842476735496533, \"x\": -7.800000000000008, \"sequenceNumber\": 11}, {\"y\": 20.89863082599834, \"x\": -7.6000000000000085, \"sequenceNumber\": 12}, {\"y\": 28.152988781234825, \"x\": -7.400000000000009, \"sequenceNumber\": 13}, {\"y\": 36.373967919509774, \"x\": -7.20000000000001, \"sequenceNumber\": 14}, {\"y\": 45.12503131882727, \"x\": -7.000000000000011, \"sequenceNumber\": 15}, {\"y\": 53.84098415416858, \"x\": -6.800000000000011, \"sequenceNumber\": 16}, {\"y\": 61.965505775989094, \"x\": -6.600000000000012, \"sequenceNumber\": 17}, {\"y\": 69.08423980422725, \"x\": -6.400000000000013, \"sequenceNumber\": 18}, {\"y\": 74.99125851865332, \"x\": -6.2000000000000135, \"sequenceNumber\": 19}, {\"y\": 79.67538410303582, \"x\": -6.000000000000014, \"sequenceNumber\": 20}, {\"y\": 83.25791597380483, \"x\": -5.800000000000015, \"sequenceNumber\": 21}, {\"y\": 85.92289736413555, \"x\": -5.600000000000016, \"sequenceNumber\": 22}, {\"y\": 87.86466500443292, \"x\": -5.400000000000016, \"sequenceNumber\": 23}, {\"y\": 89.25821830795377, \"x\": -5.200000000000017, \"sequenceNumber\": 24}, {\"y\": 90.24749981007409, \"x\": -5.000000000000018, \"sequenceNumber\": 25}, {\"y\": 90.94437188085135, \"x\": -4.8000000000000185, \"sequenceNumber\": 26}, {\"y\": 91.43259063179673, \"x\": -4.600000000000019, \"sequenceNumber\": 27}, {\"y\": 91.77332258435578, \"x\": -4.40000000000002, \"sequenceNumber\": 28}, {\"y\": 92.01048724895065, \"x\": -4.200000000000021, \"sequenceNumber\": 29}]}";


            ProcessBuilder pb = new ProcessBuilder("python", "/Users/orchie/PycharmProjects/testchart/cmdlinetest.py", jsonDoseResponse);
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
