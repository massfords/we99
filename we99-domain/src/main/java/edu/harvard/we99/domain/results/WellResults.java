package edu.harvard.we99.domain.results;

import edu.harvard.we99.domain.BaseEntity;
import edu.harvard.we99.domain.Coordinate;

import javax.annotation.Generated;
import java.util.ArrayList;
import java.util.List;

/**
 * Contains zero or more samples for a given well on a given plate.
 *
 * @author mford
 */
public class WellResults extends BaseEntity {
    private Long id;

    /**
     * The coordinate of the well for these results
     */
    private Coordinate coordinate;

    private ResultStatus resultStatus = ResultStatus.INCLUDED;

    /**
     * All of the samples associated w/ this well's results. This may be multiple
     * samples from a single analysis or they may be the same analysis but at
     * different periods of time.
     */
    private List<Sample> samples = new ArrayList<>();

    @SuppressWarnings("UnusedDeclaration")
    public WellResults() {}

    public Sample getByLabel(String lbl) {
        for(Sample s : samples) {
            if (s.getLabel().equals(lbl)) {
                return s;
            }
        }
        throw new IllegalArgumentException("unknown label");
    }

    @Generated(value = "generated by IDE")
    public Coordinate getCoordinate() {
        return coordinate;
    }

    @Generated(value = "generated by IDE")
    public void setCoordinate(Coordinate coordinate) {
        this.coordinate = coordinate;
    }

    @Generated(value = "generated by IDE")
    public List<Sample> getSamples() {
        return samples;
    }

    @Generated(value = "generated by IDE")
    public void setSamples(List<Sample> samples) {
        this.samples = samples;
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
    public ResultStatus getResultStatus() {
        return resultStatus;
    }

    @Generated(value = "generated by IDE")
    public void setResultStatus(ResultStatus resultStatus) {
        this.resultStatus = resultStatus;
    }
}
