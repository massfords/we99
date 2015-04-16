package edu.harvard.we99.domain;

import edu.harvard.we99.domain.results.DoseResponseResult;

import javax.annotation.Generated;
import javax.xml.bind.annotation.XmlTransient;

/**
 * @author alan orcharton
 */
public class AbstractPoint<T extends AbstractPoint> extends BaseEntity {

    private Long id;

    private Double x;
    private Double y;

    private Double someText;

    @XmlTransient
    protected DoseResponseResult associatedDoseResponseResult;



    @Generated("generated by IDE")
    public Long getId() {
        return id;
    }

    @Generated("generated by IDE")
    public T setId(Long id) {
        this.id = id;
        return (T) this;
    }


    @Generated("generated by IDE")
    public DoseResponseResult getAssociatedDoseResponseResult() {
        return associatedDoseResponseResult;
    }

    @Generated("generated by IDE")
    public T setAssociatedDoseResponseResult(DoseResponseResult associatedDoseResponseResult) {
        this.associatedDoseResponseResult = associatedDoseResponseResult;
        return (T) this;
    }

    @Generated("generated by IDE")
    public Double getSomeText() {
        return someText;
    }

    @Generated("generated by IDE")
    public T setSomeText(Double someText) {
        this.someText = someText;
        return (T) this;
    }


    @Generated("generated by IDE")
    public Double getX() {
        return x;
    }

    @Generated("generated by IDE")
    public T setX(Double x) {
        this.x = x;
        return (T) this;
    }

    @Generated("generated by IDE")
    public Double getY() {
        return y;
    }

    @Generated("generated by IDE")
    public T setY(Double y) {
        this.y = y;
        return (T) this;
    }
}
