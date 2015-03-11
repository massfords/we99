package edu.harvard.we99.services.storage;

import edu.harvard.we99.domain.PlateDimension;
import edu.harvard.we99.domain.PlateType;
import edu.harvard.we99.services.storage.entities.Mappers;
import edu.harvard.we99.services.storage.entities.PlateTypeEntity;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of the PlateTypeStorage interface
 *
 * @author mford
 */
public class PlateTypeStorageImpl implements PlateTypeStorage {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<PlateType> listAll() {
        TypedQuery<PlateTypeEntity> query = em.createQuery("select pt from PlateTypeEntity pt", PlateTypeEntity.class);
        List<PlateTypeEntity> resultList = query.getResultList();
        List<PlateType> list = map(resultList);
        return list;
    }

    @Override
    public List<PlateType> findGreaterThanOrEqualTo(PlateDimension dim) {
        TypedQuery<PlateTypeEntity> query = em.createQuery(
                "select pt from PlateTypeEntity pt where pt.dim.rows>=:rows and pt.dim.cols>=:cols", PlateTypeEntity.class);
        query.setParameter("rows", dim.getRows());
        query.setParameter("cols", dim.getCols());
        List<PlateTypeEntity> resultList = query.getResultList();
        return map(resultList);
    }

    @Override
    @Transactional
    public PlateType create(PlateType type) {
        PlateTypeEntity pte = Mappers.PLATETYPE.mapReverse(type);
        em.persist(pte);
        return Mappers.PLATETYPE.map(pte);
    }

    @Override
    public PlateType get(Long id) throws EntityNotFoundException {
        PlateTypeEntity pte = em.find(PlateTypeEntity.class, id);
        return Mappers.PLATETYPE.map(pte);
    }

    @Override
    @Transactional
    public PlateType update(Long id, PlateType type) throws EntityNotFoundException {
        PlateTypeEntity pte = em.find(PlateTypeEntity.class, id);
        Mappers.PLATETYPE.mapReverse(type, pte);
        em.merge(pte);
        return Mappers.PLATETYPE.map(pte);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        PlateTypeEntity pte = em.find(PlateTypeEntity.class, id);
        em.remove(pte);
    }

    private List<PlateType> map(List<PlateTypeEntity> resultList) {
        List<PlateType> list = new ArrayList<>();
        resultList.forEach(pte -> list.add(Mappers.PLATETYPE.map(pte)));
        return list;
    }

}
