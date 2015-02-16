package edu.harvard.we99.domain;

import javax.annotation.Generated;
import javax.persistence.CascadeType;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * A Well is a physical vessel within a Plate or PlateTemplate. It can receive
 * multiple doses of a compound. Our system of plates are rectangluar so
 * a Well has a simple row,col style Coordinate to identify it within a plate.
 *
 * @author mford
 */
@Entity
public class Well extends BaseEntity {

    /**
     * Primary key for this entity is generated
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Coordinate identifies the row,col for the Well
     */
    @Embedded
    private Coordinate coordinate;

    /**
     * User provided label for the Well
     *
     * todo - we probably need a map of name/value pairs here
     */
    private String label;

    /**
     * The well type tells us if this is a regular measured dose of a compound
     * or a special well like a positive, negative control or empty.
     */
    @NotNull
    private WellType type;

    /**
     * The contents of a well are one or more Doses of a compound
     */
    @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL)
    private Set<Dose> contents = new LinkedHashSet<>();

    public Well() {}

    public Well(int row, int col) {
        this.coordinate = new Coordinate(row, col);
    }

    public Well(Coordinate coordinate) {
        this.coordinate = coordinate;
    }

    /**
     * Adds all of the doses in the array into our well.
     * @param doses
     * @return
     */
    public Well dose(Dose...doses) {
        contents.addAll(Arrays.asList(doses));
        return this;
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
    public Well withCoordinate(final Coordinate coordinate) {
        this.coordinate = coordinate;
        return this;
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
    public String getLabel() {
        return label;
    }

    @Generated(value = "generated by IDE")
    public void setLabel(String label) {
        this.label = label;
    }

    @Generated(value = "generated by IDE")
    public WellType getType() {
        return type;
    }

    @Generated(value = "generated by IDE")
    public void setType(WellType type) {
        this.type = type;
    }

    @Generated(value = "generated by IDE")
    public Set<Dose> getContents() {
        return contents;
    }

    @Generated(value = "generated by IDE")
    public void setContents(Set<Dose> contents) {
        this.contents = contents;
    }

    @Generated(value = "generated by IDE")
    public Well withLabel(final String label) {
        this.label = label;
        return this;
    }

    @Generated(value = "generated by IDE")
    public Well withType(final WellType type) {
        this.type = type;
        return this;
    }

    @Override
    @Generated(value = "generated by IDE")
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Well well = (Well) o;

        if (!coordinate.equals(well.coordinate)) return false;

        return true;
    }

    @Override
    @Generated(value = "generated by IDE")
    public int hashCode() {
        return coordinate.hashCode();
    }
}

