package edu.harvard.we99.domain;

import javax.annotation.Generated;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlTransient;

/**
 * A dose is the amount (quantity and units) of a compound placed in a well.
 * Some wells comes preloaded with doses while others are empty. Even if a well
 * is pre-loaded, doses can always be changed by the scientist as part of their
 * experiment
 *
 * @author mford
 */
public class Dose extends BaseEntity {
    /**
     * Primary key for this entity is generated
     */
    private Long id;

    @NotNull
    private Amount amount;

    /**
     * The compound that is in the well. If a Well is empty, it shouldn't have
     * a dose.
     */
    //@NotNull
    // allowing null here to support the bulk add plates call that pairs a PlateMapMergeInfo with a List<Compound>
    private Compound compound;

    @XmlTransient
    private Well well;

    @XmlTransient
    private Long plateId;

    public Dose() {}

    public Dose(Compound compound, Amount amount) {
        this.compound = compound;
        this.amount = amount;
    }

    /**
     * Creates a diluted version of this dose given the provided dilution factor.
     * @param dilutionFactor
     * @return
     */
    public Dose dilute(Double dilutionFactor) {
        return new Dose(compound, getAmount().dilute(dilutionFactor));
    }

    @Generated("generated by IDE")
    public Long getId() {
        return id;
    }

    @Generated("generated by IDE")
    public Dose setId(Long id) {
        this.id = id;
        return this;
    }

    @Generated("generated by IDE")
    public Amount getAmount() {
        return amount;
    }

    @Generated("generated by IDE")
    public Dose setAmount(Amount amount) {
        this.amount = amount;
        return this;
    }

    @Generated("generated by IDE")
    public Compound getCompound() {
        return compound;
    }

    @Generated("generated by IDE")
    public Dose setCompound(Compound compound) {
        this.compound = compound;
        return this;
    }

    @Override
    @Generated(value = "generated by IDE")
    public String toString() {
        return "Dose{" +
                "amount=" + amount +
                ", compound=" + compound +
                '}';
    }

    @Generated("generated by IDE")
    public Well getWell() {
        return well;
    }

    @Generated("generated by IDE")
    public Dose setWell(Well well) {
        this.well = well;
        return this;
    }

    @Generated("generated by IDE")
    public Long getPlateId() {
        return plateId;
    }

    @Generated("generated by IDE")
    public Dose setPlateId(Long plateId) {
        this.plateId = plateId;
        return this;
    }
}
