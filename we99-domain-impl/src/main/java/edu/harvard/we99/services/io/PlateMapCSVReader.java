package edu.harvard.we99.services.io;

import edu.harvard.we99.domain.PlateMap;
import edu.harvard.we99.domain.WellMap;
import org.beanio.BeanReader;
import org.beanio.StreamFactory;

import java.io.Reader;

/**
 * @author mford
 */
public class PlateMapCSVReader {
    private final StreamFactory factory;

    public PlateMapCSVReader() {
        this("platemapping.xml");
    }

    public PlateMapCSVReader(String config) {
        factory = StreamFactory.newInstance();
        factory.loadResource(config);
    }

    public PlateMap read(Reader r) {

        PlateMap pm = new PlateMap();

        // create a BeanReader to read from "input.csv"
        BeanReader in = factory.createReader("wellMaps", r);

        try {
            Object record;
            while ((record = in.read()) != null) {
                WellMap wellMap = (WellMap) record;
                pm.withWells(wellMap);
            }
        } finally {
            in.close();
        }

        // need to validate it

        return pm;
    }
}
