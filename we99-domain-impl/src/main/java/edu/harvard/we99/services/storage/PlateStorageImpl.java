package edu.harvard.we99.services.storage;

import edu.harvard.we99.domain.Plate;

/**
 * @author mford
 */
public class PlateStorageImpl extends CRUDStorageImpl<Plate> implements PlateStorage {
    public PlateStorageImpl() {
        super(Plate.class);
    }

    @Override
    protected void updateFromCaller(Plate fromDb, Plate fromUser) {
        // todo need to impl
    }
}
