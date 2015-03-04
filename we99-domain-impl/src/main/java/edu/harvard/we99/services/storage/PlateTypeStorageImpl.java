package edu.harvard.we99.services.storage;

import edu.harvard.we99.domain.PlateDimension;
import edu.harvard.we99.domain.PlateType;

import javax.persistence.TypedQuery;
import java.util.List;

/**
 * Implementation of the PlateTypeStorage interface
 *
 * @author mford
 */
public class PlateTypeStorageImpl extends CRUDStorageImpl<PlateType> implements PlateTypeStorage {

    public PlateTypeStorageImpl() {
        super(PlateType.class);
    }

    @Override
    protected void updateFromCaller(PlateType fromDb, PlateType fromUser) {
        fromDb.withDim(fromUser.getDim())
                .withManufacturer(fromUser.getManufacturer());
    }

    @Override
    public List<PlateType> listAll() {
        TypedQuery<PlateType> query = em.createQuery("select pt from PlateType pt", PlateType.class);
        return query.getResultList();
    }

    @Override
    public List<PlateType> findGreaterThanOrEqualTo(PlateDimension dim) {
        TypedQuery<PlateType> query = em.createQuery(
                "select pt from PlateType pt where pt.dim.rows>=:rows and pt.dim.cols>=:cols", PlateType.class);
        query.setParameter("rows", dim.getRows());
        query.setParameter("cols", dim.getCols());

        return query.getResultList();
    }
}
