package edu.harvard.we99.services.storage;

import edu.harvard.we99.domain.Protocol;

import javax.persistence.TypedQuery;
import java.util.List;

/**
 * @author mford
 */
public class ProtocolStorageImpl extends CRUDStorageImpl<Protocol> implements ProtocolStorage {

    public ProtocolStorageImpl() {
        super(Protocol.class);
    }

    @Override
    protected void updateFromCaller(Protocol fromDb, Protocol fromUser) {
        fromDb.setName(fromUser.getName());
    }

    @Override
    public List<Protocol> listAll() {
        TypedQuery<Protocol> query = em.createQuery("select p from Protocol p", Protocol.class);
        return query.getResultList();
    }
}
