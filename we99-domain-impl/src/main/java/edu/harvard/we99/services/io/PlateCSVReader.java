package edu.harvard.we99.services.io;

import edu.harvard.we99.domain.Amount;
import edu.harvard.we99.domain.Compound;
import edu.harvard.we99.domain.Dose;
import edu.harvard.we99.domain.Plate;
import edu.harvard.we99.domain.Well;
import edu.harvard.we99.domain.results.PlateResult;
import edu.harvard.we99.domain.results.WellResults;
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

    public PlateWithOptionalResults read(Reader r) {

        Plate p = new Plate();
        PlateResult results = null;

        // create a BeanReader to read from "input.csv"
        BeanReader in = factory.createReader("well", r);

        try {
            Object record;
            while ((record = in.read()) != null) {
                WellRow wellRow = (WellRow) record;
                Well existing = p.getWells().get(wellRow.getCoordinate());
                if (existing == null) {
                    existing = new Well();
                    existing.setCoordinate(wellRow.getCoordinate());
                    p.withWells(existing);
                }
                existing.setType(wellRow.getType());
                existing.getContents().add(new Dose(new Compound(wellRow.getCompoundName()), new Amount(wellRow.getAmount(), wellRow.getUnits())));
                existing.withLabel(wellRow.getLabel().getName(), wellRow.getLabel().getValue());

                if (wellRow.getSample() != null) {
                    if (results == null) {
                        results = new PlateResult();
                    }
                    WellResults wellResult = new WellResults(wellRow.getCoordinate());
                    wellResult.addSample(wellRow.getSample());
                    results.getWellResults().put(wellRow.getCoordinate(), wellResult);
                }
            }
        } finally {
            in.close();
        }

        // need to validate it

        return new PlateWithOptionalResults(p, results);
    }
}
