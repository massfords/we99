package edu.harvard.we99.services.storage;

import com.mysema.query.jpa.impl.JPAQuery;
import edu.harvard.we99.domain.PlateDimension;
import edu.harvard.we99.domain.PlateType;
import edu.harvard.we99.domain.lists.PlateTypes;
import edu.harvard.we99.services.storage.entities.Mappers;
import edu.harvard.we99.services.storage.entities.PlateTypeEntity;
import edu.harvard.we99.services.storage.entities.QPlateTypeEntity;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
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

        JPAQuery query = new JPAQuery(em);
        query.from(QPlateTypeEntity.plateTypeEntity);
        long count = query.count();
        query.limit(pageSize()).offset(pageToFirstResult(page));

        List<PlateTypeEntity> resultList = query.list(QPlateTypeEntity.plateTypeEntity);
        List<PlateType> list = map(resultList);
        return new PlateTypes(count, page, list);
    }

    @Override
    public PlateTypes findGreaterThanOrEqualTo(PlateDimension dim, Integer page) {

        JPAQuery query = new JPAQuery(em);
        QPlateTypeEntity pte = QPlateTypeEntity.plateTypeEntity;
        query.from(pte)
                .where(pte.dim.rows.goe(dim.getRows())
                        .and(pte.dim.cols.goe(dim.getCols())));
        long count = query.count();
        query.limit(pageSize()).offset(pageToFirstResult(page));

        List<PlateTypeEntity> resultList = query.list(pte);
        return new PlateTypes(count, page, map(resultList));
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
