package edu.harvard.we99.domain.results;

import javax.annotation.Generated;

/**
 * @author mford
 */
public class PlateMetrics {
    private String label;
    private Double avgPositive;
    private Double avgNegative;
    private Double zee;
    private Double zeePrime;

    @Generated(value = "generated by IDE")
    public Double getAvgNegative() {
        return avgNegative;
    }

    @Generated(value = "generated by IDE")
    public void setAvgNegative(Double avgNegative) {
        this.avgNegative = avgNegative;
    }

    @Generated(value = "generated by IDE")
    public Double getAvgPositive() {
        return avgPositive;
    }

    @Generated(value = "generated by IDE")
    public void setAvgPositive(Double avgPositive) {
        this.avgPositive = avgPositive;
    }

    @Generated(value = "generated by IDE")
    public Double getZee() {
        return zee;
    }

    @Generated(value = "generated by IDE")
    public void setZee(Double zee) {
        this.zee = zee;
    }

    @Generated(value = "generated by IDE")
    public Double getZeePrime() {
        return zeePrime;
    }

    @Generated(value = "generated by IDE")
    public void setZeePrime(Double zeePrime) {
        this.zeePrime = zeePrime;
    }

    @Generated(value = "generated by IDE")
    public String getLabel() {
        return label;
    }

    @Generated(value = "generated by IDE")
    public void setLabel(String label) {
        this.label = label;
    }
}