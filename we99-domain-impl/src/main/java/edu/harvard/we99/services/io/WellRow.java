package edu.harvard.we99.services.io;

import edu.harvard.we99.domain.Coordinate;
import edu.harvard.we99.domain.DoseUnit;
import edu.harvard.we99.domain.Label;
import edu.harvard.we99.domain.WellType;

import javax.annotation.Generated;

/**
 * @author mford
 */
public class WellRow {
    private Coordinate coordinate;
    private Label label;
    private WellType type;
    private String compoundName;
    private double amount;
    private DoseUnit units;

    @Generated(value = "generated by IDE")
    public double getAmount() {
        return amount;
    }

    @Generated(value = "generated by IDE")
    public void setAmount(double amount) {
        this.amount = amount;
    }

    @Generated(value = "generated by IDE")
    public String getCompoundName() {
        return compoundName;
    }

    @Generated(value = "generated by IDE")
    public void setCompoundName(String compoundName) {
        this.compoundName = compoundName;
    }

    @Generated(value = "generated by IDE")
    public Coordinate getCoordinate() {
        return coordinate;
    }

    @Generated(value = "generated by IDE")
    public void setCoordinate(Coordinate coordinate) {
        this.coordinate = coordinate;
    }

    @Generated(value = "generated by IDE")
    public Label getLabel() {
        return label;
    }

    @Generated(value = "generated by IDE")
    public void setLabel(Label label) {
        this.label = label;
    }

    @Generated(value = "generated by IDE")
    public DoseUnit getUnits() {
        return units;
    }

    @Generated(value = "generated by IDE")
    public void setUnits(DoseUnit units) {
        this.units = units;
    }

    @Generated(value = "generated by IDE")
    public WellType getType() {
        return type;
    }

    @Generated(value = "generated by IDE")
    public void setType(WellType type) {
        this.type = type;
    }
}
