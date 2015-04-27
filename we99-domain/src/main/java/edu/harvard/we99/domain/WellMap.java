package edu.harvard.we99.domain;

/**
 * Type of well used for plate maps. It doesn't have any data associated with it,
 * just a coordinate, labels, and a type
 *
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
