package edu.harvard.we99.services.io;

import edu.harvard.we99.domain.results.PlateResult;
import edu.harvard.we99.domain.results.WellResults;
import org.beanio.BeanReader;
import org.beanio.StreamFactory;

import java.io.Reader;

/**
 * @author mford
 */
public class PlateResultCSVReader {
    private final StreamFactory factory;

    public PlateResultCSVReader() {
        this("resultsmapping.xml");
    }

    public PlateResultCSVReader(String config) {
        factory = StreamFactory.newInstance();
        factory.loadResource(config);
    }

    public PlateResult read(Reader r) {

        PlateResult pr = new PlateResult();

        // create a BeanReader to read from "input.csv"
        BeanReader in = factory.createReader("results", r);

        try {
            Object record;
            while ((record = in.read()) != null) {
                WellResults result = (WellResults) record;
                WellResults existing = pr.getWellResults().get(result.getCoordinate());
                if (existing == null) {
                    pr.getWellResults().put(result.getCoordinate(), result);
                } else {
                    existing.getSamples().addAll(result.getSamples());
                }
            }
        } finally {
            in.close();
        }

        // need to validate it

        return pr;
    }

}
