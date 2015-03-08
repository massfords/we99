package edu.harvard.we99.services.storage;

import edu.harvard.we99.domain.Plate;

import javax.persistence.TypedQuery;
import java.util.List;

/**
 * @author mford
 */
public class PlateStorageImpl extends CRUDStorageImpl<Plate> implements PlateStorage {
    public PlateStorageImpl() {
        super(Plate.class);
    }

    @Override
    protected void updateFromCaller(Plate fromDb, Plate fromUser) {
        fromDb.withName(fromUser.getName());
        fromDb.withBarcode(fromUser.getBarcode());
        fromDb.withDescription(fromUser.getDescription());
    }

    @Override
    public List<Plate> listAll(Long experimentId) {
        TypedQuery<Plate> query = em.createQuery(
                "select p from Plate p where p.experiment.id=:id", Plate.class);
        query.setParameter("id", experimentId);
        return query.getResultList();
    }
}
