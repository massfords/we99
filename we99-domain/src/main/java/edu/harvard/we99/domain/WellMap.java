package edu.harvard.we99.domain;

/**
 * @author mford
 */
public class WellMap extends AbstractWell<WellMap> {

    public WellMap() {}

    public WellMap(Coordinate coord) {
        super(coord);
    }

    public WellMap(int row, int col) {
        super(row, col);
    }
}
