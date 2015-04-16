package edu.harvard.we99.services.io;

import edu.harvard.we99.domain.results.PlateResult;

import java.io.IOException;
import java.io.Reader;

/**
 * @author markford
 */
public interface PlateResultsReader {
    PlateResult read(Reader r) throws IOException;
}
