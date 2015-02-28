package edu.harvard.we99.services.storage;

import edu.harvard.we99.domain.Compound;

/**
 * @author mford
 */
public class CompoundStorageImpl extends CRUDStorageImpl<Compound> implements CRUDStorage<Compound> {

    public CompoundStorageImpl() {
        super(Compound.class);
    }

    @Override
    protected void updateFromCaller(Compound fromDb, Compound fromUser) {
        fromDb.withName(fromUser.getName());
    }
}
