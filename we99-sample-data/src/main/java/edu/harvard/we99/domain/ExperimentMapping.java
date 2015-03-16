package edu.harvard.we99.domain;

import javax.annotation.Generated;

/**
 * @author mford
 */
public class ExperimentMapping {
    private String name;
    private int numberOfPlates;
    private ExperimentStatus status;
    private String protocol;
    private String desc;

    @Generated(value = "generated by IDE")
    public String getDesc() {
        return desc;
    }

    @Generated(value = "generated by IDE")
    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Generated(value = "generated by IDE")
    public String getName() {
        return name;
    }

    @Generated(value = "generated by IDE")
    public void setName(String name) {
        this.name = name;
    }

    @Generated(value = "generated by IDE")
    public int getNumberOfPlates() {
        return numberOfPlates;
    }

    @Generated(value = "generated by IDE")
    public void setNumberOfPlates(int numberOfPlates) {
        this.numberOfPlates = numberOfPlates;
    }

    @Generated(value = "generated by IDE")
    public String getProtocol() {
        return protocol;
    }

    @Generated(value = "generated by IDE")
    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    @Generated(value = "generated by IDE")
    public ExperimentStatus getStatus() {
        return status;
    }

    @Generated(value = "generated by IDE")
    public void setStatus(ExperimentStatus status) {
        this.status = status;
    }
}
