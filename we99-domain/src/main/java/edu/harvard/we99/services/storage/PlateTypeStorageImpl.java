package edu.harvard.we99.services.storage;

import edu.harvard.we99.domain.PlateType;

/**
 * Implementation of the PlateTypeStorage interface
 *
 * @author mford
 */
public class PlateTypeStorageImpl extends CRUDStorageImpl<PlateType> implements CRUDStorage<PlateType> {

    public PlateTypeStorageImpl() {
        super(PlateType.class);
    }

    @Override
    protected void updateFromCaller(PlateType fromDb, PlateType fromUser) {
        fromDb.withDim(fromUser.getDim())
                .withManufacturer(fromUser.getManufacturer());
    }
}
