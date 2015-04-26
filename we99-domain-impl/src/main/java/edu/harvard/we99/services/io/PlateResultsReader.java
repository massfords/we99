package edu.harvard.we99.services.io;

import java.io.IOException;
import java.io.Reader;

/**
 * @author markford
 */
public interface PlateResultsReader {
    void read(Reader r, PlateResultCollector collector) throws IOException;
}
