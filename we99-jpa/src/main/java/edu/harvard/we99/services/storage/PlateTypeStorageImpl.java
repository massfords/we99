package edu.harvard.we99.services.storage;

import edu.harvard.we99.domain.PlateDimension;
import edu.harvard.we99.domain.PlateType;
import edu.harvard.we99.domain.lists.PlateTypes;
import edu.harvard.we99.services.storage.entities.Mappers;
import edu.harvard.we99.services.storage.entities.PlateTypeEntity;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;

import static edu.harvard.we99.services.EntityListingSettings.pageSize;
import static edu.harvard.we99.services.EntityListingSettings.pageToFirstResult;

/**
 * Implementation of the PlateTypeStorage interface
 *
 * @author mford
 */
public class PlateTypeStorageImpl implements PlateTypeStorage {

    @PersistenceContext
    private EntityManager em;

    @Override
    public PlateTypes listAll(Integer page) {
        TypedQuery<PlateTypeEntity> query = em.createQuery("select pt from PlateTypeEntity pt",
                PlateTypeEntity.class);
        query.setFirstResult(pageToFirstResult(page));
        query.setMaxResults(pageSize());
        List<PlateTypeEntity> resultList = query.getResultList();
        List<PlateType> list = map(resultList);
        return new PlateTypes(count(), page, list);
    }
    private Long count() {
        TypedQuery<Long> q = em.createQuery(
                "select count(e) from PlateTypeEntity e", Long.class);
        return q.getSingleResult();
    }

    @Override
    public PlateTypes findGreaterThanOrEqualTo(PlateDimension dim, Integer page) {
        TypedQuery<PlateTypeEntity> query = em.createQuery(
                "select pt from PlateTypeEntity pt where pt.dim.rows>=:rows and pt.dim.cols>=:cols", PlateTypeEntity.class);
        query.setParameter("rows", dim.getRows());
        query.setParameter("cols", dim.getCols());
        query.setFirstResult(pageToFirstResult(page));
        query.setMaxResults(pageSize());
        List<PlateTypeEntity> resultList = query.getResultList();
        return new PlateTypes(countDim(dim), page, map(resultList));
    }
    private Long countDim(PlateDimension dim) {
        TypedQuery<Long> query = em.createQuery(
                "select count(pt) from PlateTypeEntity pt where pt.dim.rows>=:rows and pt.dim.cols>=:cols",
                Long.class);
        query.setParameter("rows", dim.getRows());
        query.setParameter("cols", dim.getCols());
        return query.getSingleResult();
    }

    @Override
    @Transactional
    public PlateType create(PlateType type) {
        assert type.getId() == null;
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
