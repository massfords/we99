package edu.harvard.we99.services.storage;

import edu.harvard.we99.domain.Plate;

/**
 * @author mford
 */
public class PlateStorageImpl extends CRUDStorageImpl<Plate> {
    public PlateStorageImpl(Class<Plate> clazz) {
        super(clazz);
    }

    @Override
    protected void updateFromCaller(Plate fromDb, Plate fromUser) {

    }
}
