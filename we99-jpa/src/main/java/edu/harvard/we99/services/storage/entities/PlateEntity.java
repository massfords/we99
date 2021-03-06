package edu.harvard.we99.services.storage.entities;

import edu.harvard.we99.domain.Coordinate;
import edu.harvard.we99.domain.PlateDimension;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import javax.annotation.Generated;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.MapKey;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PersistenceException;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author mford
 */
@Entity
@Table(name = "plates")
public class PlateEntity extends AbstractPlateEntity<PlateEntity> {
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
    private String barcode;

    /**
     * A plate consists of Wells in which the compounds we want to test are placed.
     * Each well is uniquely identified by a row,col coordinate within this plate
     * so we'll store them according to their coordinate.
     */
    @OneToMany(orphanRemoval = true, cascade = CascadeType.PERSIST)
    @Size(min = 0)
    @MapKey(name="coordinate")
    private Map<Coordinate, WellEntity> wells = new LinkedHashMap<>();

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(updatable = false)
    @NotNull
    private ExperimentEntity experiment;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<LabelEntity> labels = new HashSet<>();

    @OneToOne(orphanRemoval = true, optional = true)
    private PlateResultEntity results;

    /**
     * Every plate has a plate type that identifies its size, orientation, and manufacturer.
     * There may be 100's of these plate types in the system
     */
    @ManyToOne
    @NotNull
    @JoinColumn(updatable = false)
    private PlateTypeEntity plateType;

    /**
     * when the plate was added to the experiment. The order is important because
     * lots of the import calls don't provide barcodes so we'll rely on the natural
     * order of the plates to associate them with their results
     */
    @NotNull
    @Type(type="org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime created;

    @PrePersist
    @PreUpdate
    private void validate() {
        checkWells(wells.keySet());
        if (created == null) {
            created = DateTime.now();
        }
    }

    /**
     * Validation check that we don't have an invalid coordinate for a Well that
     * cannot exist within this plate based on its plate type.
     */
    protected void checkWells(Set<Coordinate> coords) {
        // get the max row and max col
        if (getPlateType() != null) {
            // note: checking for plateType != null to avoid a NPE as we're only
            // interested in validating the well coordinates here. The EntityManager
            // will handle the validation that we actually have a PlateType,
            // although after this call.
            int maxRow = 0;
            int maxCol = 0;
            for(Coordinate coord : coords) {
                maxRow = Math.max(maxRow, coord.getRow());
                maxCol = Math.max(maxCol, coord.getCol());
            }

            PlateDimension maxSpecified = new PlateDimension(maxRow, maxCol);

            if (!getPlateType().getDim().greaterThan(maxSpecified)) {
                String message = "Coordinates for wells must be with 0x0 and %dx%d";
                String formattedErrorMessage = String.format(
                        message, getPlateType().getDim().getRows(), getPlateType().getDim().getCols());
                throw new PersistenceException(formattedErrorMessage);
            }
        }
    }


    public PlateEntity withWells(WellEntity...wells) {
        for(WellEntity we : wells) {
            this.wells.put(we.getCoordinate(), we);
        }
        return this;
    }

    public void addWell(WellEntity w) {
        this.wells.put(w.getCoordinate(), w);
    }

    public Integer getWellCount() {
        return wells.size();
    }

    // this is for the mappers to easily set the flag on the domain object.
    // It could be replaced with a custom converter instead.
    public boolean isHasResults() {
        return this.results != null;
    }

    @Generated("generated by IDE")
    public Long getDerivedFrom() {
        return derivedFrom;
    }

    @Generated("generated by IDE")
    public PlateEntity setDerivedFrom(Long derivedFrom) {
        this.derivedFrom = derivedFrom;
        return this;
    }

    @Generated("generated by IDE")
    public String getBarcode() {
        return barcode;
    }

    @Generated("generated by IDE")
    public PlateEntity setBarcode(String barcode) {
        this.barcode = barcode;
        return this;
    }

    @Generated("generated by IDE")
    public Map<Coordinate, WellEntity> getWells() {
        return wells;
    }

    @Generated("generated by IDE")
    public PlateEntity setWells(Map<Coordinate, WellEntity> wells) {
        this.wells = wells;
        return this;
    }

    @Generated("generated by IDE")
    public ExperimentEntity getExperiment() {
        return experiment;
    }

    @Generated("generated by IDE")
    public PlateEntity setExperiment(ExperimentEntity experiment) {
        this.experiment = experiment;
        return this;
    }

    @Generated("generated by IDE")
    public Set<LabelEntity> getLabels() {
        return labels;
    }

    @Generated("generated by IDE")
    public PlateEntity setLabels(Set<LabelEntity> labels) {
        this.labels = labels;
        return this;
    }

    @Generated("generated by IDE")
    public PlateResultEntity getResults() {
        return results;
    }

    @Generated("generated by IDE")
    public PlateEntity setResults(PlateResultEntity results) {
        this.results = results;
        return this;
    }

    @Generated("generated by IDE")
    public PlateTypeEntity getPlateType() {
        return plateType;
    }

    @Generated("generated by IDE")
    public PlateEntity setPlateType(PlateTypeEntity plateType) {
        this.plateType = plateType;
        return this;
    }

    @Generated("generated by IDE")
    public DateTime getCreated() {
        return created;
    }

    @Generated("generated by IDE")
    public PlateEntity setCreated(DateTime created) {
        this.created = created;
        return this;
    }
}
