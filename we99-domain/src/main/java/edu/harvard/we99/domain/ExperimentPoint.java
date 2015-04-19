package edu.harvard.we99.domain;

import edu.harvard.we99.domain.results.DoseResponseResult;

import javax.annotation.Generated;
import javax.persistence.Embeddable;

/**
 * @author alan orcharton
 */
@Embeddable
public class ExperimentPoint {


    private Double x;
    private Double y;

    private Long plateId;
    private Long doseId;

    public ExperimentPoint(){}
    public ExperimentPoint(Long plateId, Long doseId){
        this.plateId = plateId;
        this.doseId = doseId;

    }

    @Generated("generated by IDE")
    public Double getX() {
        return x;
    }

    @Generated("generated by IDE")
    public ExperimentPoint setX(Double x) {
        this.x = x;
        return this;
    }

    @Generated("generated by IDE")
    public Double getY() {
        return y;
    }

    @Generated("generated by IDE")
    public ExperimentPoint setY(Double y) {
        this.y = y;
        return this;
    }

    @Generated("generated by IDE")
    public Long getPlateId() {
        return plateId;
    }

    @Generated("generated by IDE")
    public ExperimentPoint setPlateId(Long plateId) {
        this.plateId = plateId;
        return this;
    }

    @Generated("generated by IDE")
    public Long getDoseId() {
        return doseId;
    }

    @Generated("generated by IDE")
    public ExperimentPoint setDoseId(Long doseId) {
        this.doseId = doseId;
        return this;
    }

}
