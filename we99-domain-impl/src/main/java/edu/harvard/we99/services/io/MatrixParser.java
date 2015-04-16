package edu.harvard.we99.services.io;

import edu.harvard.we99.domain.Coordinate;
import edu.harvard.we99.domain.results.PlateResult;
import edu.harvard.we99.domain.results.Sample;
import edu.harvard.we99.domain.results.WellResults;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author markford
 */
public class MatrixParser implements PlateResultsReader {

    private final Pattern pattern = Pattern.compile("^ *([a-p])((?:\\s*(?:,)?\\s*[0-9]+(?:\\.[0-9])?){24})(?:\\s*)$", Pattern.CASE_INSENSITIVE);

    private final Pattern valuesPattern = Pattern.compile("([0-9]+(?:\\.[0-9])?)");

    @Override
    public PlateResult read(Reader r) throws IOException {

        PlateResult pr = new PlateResult();

        try (BufferedReader br = new BufferedReader(r)) {
            String line;
            while( (line = br.readLine()) != null) {
                Matcher matcher = pattern.matcher(line);
                if (matcher.matches()) {
//                    System.out.println("matched:" + line);
                    int row = matcher.group(1).toUpperCase().charAt(0) - 'A';
//                    String[] values = matcher.group(2).trim().split("(\\s+(,?)\\s+)");
                    Matcher values = valuesPattern.matcher(matcher.group(2));
                    int col=0;
                    int offset = 0;
                    while(values.find(offset)) {
                        String v = values.group(1);
                        Coordinate coord = new Coordinate(row, col++);
                        WellResults result = new WellResults(coord);
                        result.getSamples().add(new Sample(Double.parseDouble(v.trim())));
                        pr.getWellResults().put(result.getCoordinate(), result);
                        offset = values.end(1);
                    }
//                } else {
//                    System.out.println("failed to match: " + line);
                }
            }
        }

        // need to validate it

        return pr;
    }

}
