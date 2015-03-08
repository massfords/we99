package edu.harvard.we99.services.storage;

import edu.harvard.we99.domain.Protocol;

/**
 * @author mford
 */
public class ProtocolStorageImpl extends CRUDStorageImpl<Protocol> {

    public ProtocolStorageImpl() {
        super(Protocol.class);
    }

    @Override
    protected void updateFromCaller(Protocol fromDb, Protocol fromUser) {
        fromDb.setName(fromUser.getName());
    }
}
