package edu.harvard.we99.services.storage;

import edu.harvard.we99.domain.Compound;

import javax.persistence.TypedQuery;
import java.util.List;

/**
 * @author mford
 */
public class CompoundStorageImpl extends CRUDStorageImpl<Compound> implements CompoundStorage {

    public CompoundStorageImpl() {
        super(Compound.class);
    }

    @Override
    protected void updateFromCaller(Compound fromDb, Compound fromUser) {
        fromDb.withName(fromUser.getName());
    }

    @Override
    public List<Compound> listAll() {
        TypedQuery<Compound> query = em.createQuery("select c from Compound c", Compound.class);
        return query.getResultList();
    }
}
