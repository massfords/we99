package edu.harvard.we99.services.io;

import edu.harvard.we99.domain.results.WellResults;
import org.beanio.BeanReader;
import org.beanio.StreamFactory;

import java.io.Reader;

/**
 * @author mford
 */
public class PlateResultCSVReader implements PlateResultsReader {
    private final StreamFactory factory;

    public PlateResultCSVReader() {
        this("resultsmapping.xml");
    }

    public PlateResultCSVReader(String config) {
        factory = StreamFactory.newInstance();
        factory.loadResource(config);
    }

    @Override
    public void read(Reader r, PlateResultCollector collector) {

        // create a BeanReader to read from "input.csv"
        BeanReader in = factory.createReader("results", r);

        try {
            Object record;
            while ((record = in.read()) != null) {
                WellResults result = (WellResults) record;
                collector.collect(result);
            }
        } finally {
            in.close();
        }
    }
}
