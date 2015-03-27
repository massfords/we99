package edu.harvard.we99.services.storage.entities;

import edu.harvard.we99.domain.Coordinate;

import javax.persistence.Entity;

/**
 * @author mford
 */
@Entity
public class WellMapEntity extends AbstractWellEntity<WellMapEntity> {
    @SuppressWarnings("unused")
    public WellMapEntity() {}

    public WellMapEntity(Coordinate coord) {
        super(coord);
    }

    public WellMapEntity(int row, int col) {
        super(row, col);
    }

}
