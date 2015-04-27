package edu.harvard.we99.domain.results;

import edu.harvard.we99.domain.BaseEntity;
import edu.harvard.we99.domain.Compound;
import edu.harvard.we99.domain.CurveFitPoint;
import edu.harvard.we99.domain.Dose;
import edu.harvard.we99.domain.Experiment;
import edu.harvard.we99.domain.ExperimentPoint;
import edu.harvard.we99.domain.FitEquation;
import edu.harvard.we99.domain.FitParameter;
import edu.harvard.we99.jaxb.DateTimeAdapter;
import org.joda.time.DateTime;

import javax.annotation.Generated;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Result of Dose Response analysis for a compound
 *
 * @author alan orcharton
 */
public class DoseResponseResult extends BaseEntity {

    private Long id;
    private Compound compound;
    private Experiment experiment;

    /**
     * Creation time
     */
    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    private DateTime created;

    /**
     * Time of the last modification (computed with the bean is saved)
     *
     */
    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    private DateTime lastModified;


    /**
     * Optional comments from the user about the changes
     */
    private String comments;

    @XmlTransient
    private List<Dose> doses = new ArrayList<>();

    private List<ExperimentPoint> experimentPoints = new ArrayList<>();

    private List<CurveFitPoint> curveFitPoints = new ArrayList<>();

    private FitEquation fitEquation = FitEquation.HILLEQUATION;

    private Map<String,FitParameter> fitParameterMap = new HashMap<>();


    @Generated("generated by IDE")
    public String getComments() {
        return comments;
    }

    @Generated("generated by IDE")
    public DoseResponseResult setComments(String comments) {
        this.comments = comments;
        return this;
    }

    @Generated("generated by IDE")
    public Long getId() {
        return id;
    }

    @Generated("generated by IDE")
    public DoseResponseResult setId(Long id) {
        this.id = id;
        return this;
    }

    @Generated("generated by IDE")
    public Compound getCompound() {
        return compound;
    }

    @Generated("generated by IDE")
    public DoseResponseResult setCompound(Compound compound) {
        this.compound = compound;
        return this;
    }

    @Generated("generated by IDE")
    public DateTime getCreated() {
        return created;
    }

    @Generated("generated by IDE")
    public DoseResponseResult setCreated(DateTime created) {
        this.created = created;
        return this;
    }

    @Generated("generated by IDE")
    public DateTime getLastModified() {
        return lastModified;
    }

    @Generated("generated by IDE")
    public DoseResponseResult setLastModified(DateTime lastModified) {
        this.lastModified = lastModified;
        return this;
    }

    @Generated("generated by IDE")
    public List<ExperimentPoint> getExperimentPoints() {
        return experimentPoints;
    }

    @Generated("generated by IDE")
    public DoseResponseResult setExperimentPoints(List<ExperimentPoint> experimentPoints) {
        this.experimentPoints = experimentPoints;
        return this;
    }

    @Generated("generated by IDE")
    public Experiment getExperiment() {
        return experiment;
    }

    @Generated("generated by IDE")
    public DoseResponseResult setExperiment(Experiment experiment) {
        this.experiment = experiment;
        return this;
    }

    @Generated("generated by IDE")
    public List<CurveFitPoint> getCurveFitPoints() {
        return curveFitPoints;
    }

    @Generated("generated by IDE")
    public DoseResponseResult setCurveFitPoints(List<CurveFitPoint> curveFitPoints) {
        this.curveFitPoints = curveFitPoints;
        return this;
    }

    @Generated("generated by IDE")
    public Map<String,FitParameter> getFitParameterMap() {
        return fitParameterMap;
    }

    @Generated("generated by IDE")
    public DoseResponseResult setFitParameterMap(Map<String,FitParameter> fitParameterMap) {
        this.fitParameterMap = fitParameterMap;
        return this;
    }

    @Generated("generated by IDE")
    public FitEquation getFitEquation() {
        return fitEquation;
    }

    @Generated("generated by IDE")
    public DoseResponseResult setFitEquation(FitEquation fitEquation) {
        this.fitEquation = fitEquation;
        return this;
    }

    @Generated("generated by IDE")
    public List<Dose> getDoses() {
        return doses;
    }

    @Generated("generated by IDE")
    public DoseResponseResult setDoses(List<Dose> doses) {
        this.doses = doses;
        return this;
    }
}
