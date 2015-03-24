package edu.harvard.we99.services.io;

import edu.harvard.we99.domain.Amount;
import edu.harvard.we99.domain.Compound;
import edu.harvard.we99.domain.Dose;
import edu.harvard.we99.domain.Plate;
import edu.harvard.we99.domain.Well;
import org.beanio.BeanReader;
import org.beanio.StreamFactory;

import java.io.Reader;

/**
 * @author mford
 */
public class PlateCSVReader {
    private final StreamFactory factory;

    public PlateCSVReader() {
        this("plate.xml");
    }

    public PlateCSVReader(String config) {
        factory = StreamFactory.newInstance();
        factory.loadResource(config);
    }

    public Plate read(Reader r) {

        Plate p = new Plate();

        // create a BeanReader to read from "input.csv"
        BeanReader in = factory.createReader("well", r);

        try {
            Object record;
            while ((record = in.read()) != null) {
                WellRow well = (WellRow) record;
                Well existing = p.getWells().get(well.getCoordinate());
                if (existing == null) {
                    existing = new Well();
                    existing.setCoordinate(well.getCoordinate());
                    p.withWells(existing);
                }
                existing.setType(well.getType());
                existing.getContents().add(new Dose(new Compound(well.getCompoundName()), new Amount(well.getAmount(), well.getUnits())));
                existing.withLabel(well.getLabel().getName(), well.getLabel().getValue());
            }
        } finally {
            in.close();
        }

        // need to validate it

        return p;
    }
}
