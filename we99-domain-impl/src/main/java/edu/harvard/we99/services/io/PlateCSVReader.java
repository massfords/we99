package edu.harvard.we99.services.io;

import edu.harvard.we99.domain.Amount;
import edu.harvard.we99.domain.Compound;
import edu.harvard.we99.domain.Coordinate;
import edu.harvard.we99.domain.Dose;
import edu.harvard.we99.domain.Plate;
import edu.harvard.we99.domain.Well;
import edu.harvard.we99.domain.results.PlateResult;
import edu.harvard.we99.domain.results.WellResults;
import org.beanio.BeanReader;
import org.beanio.StreamFactory;

import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

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

    public List<PlateWithOptionalResults> read(Reader r) {

        List<PlateWithOptionalResults> porList = new ArrayList<>();

        Plate p = null;
        PlateResult results = null;

        // create a BeanReader to read from "input.csv"
        BeanReader in = factory.createReader("well", r);

        Coordinate zeroZero = new Coordinate(0,0);

        try {
            Object record;
            boolean lookForNextPlate = false;
            while ((record = in.read()) != null) {
                WellRow wellRow = (WellRow) record;

                if (p == null) {
                    // it's the first well for the first plate we've seen.
                    // create the plate and continue processing
                    p = new Plate();
                    porList.add(new PlateWithOptionalResults(p));
                } else if (wellRow.getCoordinate().equals(zeroZero) && lookForNextPlate) {
                    // we're at 0,0 and we're looking for a new plate. We must have found one.
                    // thus, add the current plate to the list and add a new one
                    p = new Plate();
                    results = null;
                } else if (!wellRow.getCoordinate().equals(zeroZero) && !lookForNextPlate) {
                    // we're passed 0,0 and reading wells into the current plate.
                    // we'll keep reading these wells into the current plate but
                    // if we see a 0,0 at any point then we'll stop
                    lookForNextPlate = true;
                }

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
                        porList.get(porList.size()-1).setResults(results);
                    }
                    WellResults wellResult = new WellResults(wellRow.getCoordinate());
                    wellResult.addSample(wellRow.getSample());
                    results.getWellResults().put(wellRow.getCoordinate(), wellResult);
                }
            }
        } finally {
            in.close();
        }

        return porList;
    }
}
