package edu.harvard.we99.services.storage.entities;

import edu.harvard.we99.domain.Coordinate;

import javax.annotation.Generated;
import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author mford
 */
@Entity
public class WellEntity extends AbstractWellEntity<WellEntity> {

    public WellEntity() {}

    public WellEntity(int row, int col) {
        setCoordinate(new Coordinate(row, col));
    }

    /**
     * The contents of a well are one or more Doses of a compound
     */
    @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL)
    private Set<DoseEntity> contents = new LinkedHashSet<>();


    @Generated("generated by IDE")
    public Set<DoseEntity> getContents() {
        return contents;
    }

    @Generated("generated by IDE")
    public WellEntity setContents(Set<DoseEntity> contents) {
        this.contents = contents;
        return this;
    }

}
