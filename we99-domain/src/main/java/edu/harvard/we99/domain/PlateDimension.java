package edu.harvard.we99.domain;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.annotation.Generated;
import javax.persistence.Embeddable;
import javax.validation.constraints.Min;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author mford
 */
@Embeddable
@XmlAccessorType(XmlAccessType.FIELD)
@JsonInclude(JsonInclude.Include.NON_NULL)
@XmlRootElement(name="dim")
public class PlateDimension {
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

    @Generated(value = "generated by IDE")
    public int getCols() {
        return cols;
    }

    @Generated(value = "generated by IDE")
    public void setCols(int cols) {
        this.cols = cols;
    }


    @Generated(value = "generated by IDE")
    public int getRows() {
        return rows;
    }

    @Generated(value = "generated by IDE")
    public void setRows(int rows) {
        this.rows = rows;
    }

    @Generated(value = "generated by IDE")
    public PlateDimension withCols(final int cols) {
        this.cols = cols;
        return this;
    }

    @Generated(value = "generated by IDE")
    public PlateDimension withRows(final int rows) {
        this.rows = rows;
        return this;
    }
}
