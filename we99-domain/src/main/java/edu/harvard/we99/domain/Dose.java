package edu.harvard.we99.domain;

import javax.annotation.Generated;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Entity
public class Dose extends BaseEntity {
    /**
     * Primary key for this entity is generated
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Embedded
    private Amount amount;

    /**
     * The compound that is in the well. If a Well is empty, it shouldn't have
     * a dose.
     *
     * todo - do we need some type of "unknown" compound
     */
    @ManyToOne @NotNull
    private Compound compound;

    /**
     * A back pointer to the parent well in which this Dose lives
     */
    @ManyToOne @JoinColumn(updatable = false)
    @XmlTransient
    private Well well;

    public Dose() {}

    public Dose(Compound compound, Amount amount) {
        this.compound = compound;
        this.amount = amount;
    }

    @Generated(value = "generated by IDE")
    public Dose withId(final Long id) {
        this.id = id;
        return this;
    }

    @Generated(value = "generated by IDE")
    public Dose withCompound(final Compound compound) {
        this.compound = compound;
        return this;
    }

    @Generated(value = "generated by IDE")
    public Dose withWell(final Well well) {
        this.well = well;
        return this;
    }

    @Generated(value = "generated by IDE")
    public Amount getAmount() {
        return amount;
    }

    @Generated(value = "generated by IDE")
    public void setAmount(Amount amount) {
        this.amount = amount;
    }

    @Generated(value = "generated by IDE")
    public Long getId() {
        return id;
    }

    @Generated(value = "generated by IDE")
    public void setId(Long id) {
        this.id = id;
    }

    @Generated(value = "generated by IDE")
    public Compound getCompound() {
        return compound;
    }

    @Generated(value = "generated by IDE")
    public void setCompound(Compound compound) {
        this.compound = compound;
    }

    @Generated(value = "generated by IDE")
    public Well getWell() {
        return well;
    }

    @Generated(value = "generated by IDE")
    public void setWell(Well well) {
        this.well = well;
    }

    @Generated(value = "generated by IDE")
    public Dose withAmount(final Amount amount) {
        this.amount = amount;
        return this;
    }

    public Dose withAmount(int amount, DoseUnit du) {
        this.amount = new Amount(amount, du);
        return this;
    }

    @Override
    @Generated(value = "generated by IDE")
    public String toString() {
        return "Dose{" +
                "amount=" + amount +
                ", compound=" + compound +
                ", well=" + well +
                '}';
    }
}
