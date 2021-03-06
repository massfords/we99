package edu.harvard.we99.domain;

import javax.annotation.Generated;
import javax.persistence.Embeddable;
import javax.validation.constraints.Min;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author mford
 */
@Embeddable
@XmlRootElement(name="dim")
public class PlateDimension extends BaseEntity {
    /**
     * The number of rows in the plate. Must be at least 1
     */
    @Min(1)
    private int rows;

    /**
     * The number of cols in the plate. Must be at least 1
     */
    @Min(1)
    private int cols;

    public PlateDimension() {}

    public PlateDimension(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
    }

    /**
     * Returns true if this plate dimension completely covers the other plate
     * dimension
     * @param other
     * @return
     */
    public boolean greaterThan(PlateDimension other) {
        return rows > other.getRows() && cols > other.getCols();
    }

    @Generated("generated by IDE")
    public int getRows() {
        return rows;
    }

    @Generated("generated by IDE")
    public PlateDimension setRows(int rows) {
        this.rows = rows;
        return this;
    }

    @Generated("generated by IDE")
    public int getCols() {
        return cols;
    }

    @Generated("generated by IDE")
    public PlateDimension setCols(int cols) {
        this.cols = cols;
        return this;
    }
}
