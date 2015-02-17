package edu.harvard.we99.domain;

import javax.annotation.Generated;
import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * A Plate is an instantiation of a PlateTemplate.
 *
 * @author mford
 */
@Entity
public class Plate extends AbstractPlate<Plate> {
    /**
     * Reference to the id of the PlateTemplate that this Plate was derived from.
     *
     * It's odd to have a Long here but I worry about referring back to the
     * PlateTemplate entity since it could change over time and make a previous
     * instance of a Plate inconsistent since the size might not match.
     */
    @Column(updatable = false)
    private Long derivedFrom;

    /**
     * Optional user provided barcode for the plate
     */
    private Long barcode;

    public Plate() {}

    public Plate(String name, PlateType type) {
        setName(name);
        setPlateType(type);
    }

    @Generated(value = "generated by IDE")
    public Long getBarcode() {
        return barcode;
    }

    @Generated(value = "generated by IDE")
    public void setBarcode(Long barcode) {
        this.barcode = barcode;
    }

    @Generated(value = "generated by IDE")
    public Long getDerivedFrom() {
        return derivedFrom;
    }

    @Generated(value = "generated by IDE")
    public void setDerivedFrom(Long derivedFrom) {
        this.derivedFrom = derivedFrom;
    }

    @Generated(value = "generated by IDE")
    public Plate withDerivedFrom(final Long derivedFrom) {
        this.derivedFrom = derivedFrom;
        return this;
    }

    @Generated(value = "generated by IDE")
    public Plate withBarcode(final Long barcode) {
        this.barcode = barcode;
        return this;
    }
}