package edu.harvard.we99.services.storage;

import edu.harvard.we99.domain.Protocol;

/**
 * @author mford
 */
public class ProtocolStorageImpl extends CRUDStorageImpl<Protocol> {

    public ProtocolStorageImpl(Class<Protocol> clazz) {
        super(clazz);
    }

    @Override
    protected void updateFromCaller(Protocol fromDb, Protocol fromUser) {

    }
}
