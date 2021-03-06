package edu.harvard.we99.domain;

import edu.harvard.we99.domain.results.ResultStatus;

import javax.annotation.Generated;
import javax.persistence.Embeddable;

/**
 * @author alan orcharton
 */
@Embeddable
public class ExperimentPoint {


    private Double x;
    private Double logx;
    private Double y;

    private Long plateId;
    private Long doseId;

    private ResultStatus resultStatus = ResultStatus.INCLUDED;

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

    @Generated("generated by IDE")
    public Double getLogx() {
        return logx;
    }

    @Generated("generated by IDE")
    public ExperimentPoint setLogx(Double logx) {
        this.logx = logx;
        return this;
    }

    @Generated("generated by IDE")
    public ResultStatus getResultStatus() {
        return resultStatus;
    }

    @Generated("generated by IDE")
    public ExperimentPoint setResultStatus(ResultStatus resultStatus) {
        this.resultStatus = resultStatus;
        return this;
    }
}
