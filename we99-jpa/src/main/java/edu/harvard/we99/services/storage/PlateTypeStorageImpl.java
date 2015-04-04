package edu.harvard.we99.services.storage;

import com.mysema.query.Tuple;
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
import javax.persistence.PersistenceException;
import java.util.ArrayList;
import java.util.List;

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
    public PlateTypes listAll(Integer page, Integer pageSize) {

        JPAQuery query = new JPAQuery(em);
        QPlateTypeEntity pte = QPlateTypeEntity.plateTypeEntity;
        query.from(pte).leftJoin(pte.plates).groupBy(pte);
        query.limit(pageSize).offset(pageToFirstResult(page, pageSize));

        long count = new JPAQuery(em).from(pte).count();

        List<Tuple> tuples = query.list(pte, pte.count());
        List<PlateType> list = map(tuples);
        return new PlateTypes(count, page, pageSize, list);
    }

    @Override
    public PlateType getByName(String plateTypeName) {
        JPAQuery query = new JPAQuery(em);
        query.from(QPlateTypeEntity.plateTypeEntity).where(QPlateTypeEntity.plateTypeEntity.name.eq(plateTypeName));
        return Mappers.PLATETYPE.map(query.uniqueResult(QPlateTypeEntity.plateTypeEntity));
    }

    @Override
    public PlateTypes findGreaterThanOrEqualTo(PlateDimension dim, Integer page, Integer pageSize) {

        JPAQuery query = new JPAQuery(em);
        QPlateTypeEntity pte = QPlateTypeEntity.plateTypeEntity;
        query.from(pte)
                .where(pte.dim.rows.goe(dim.getRows())
                        .and(pte.dim.cols.goe(dim.getCols())));
        long count = query.count();
        query.limit(pageSize).offset(pageToFirstResult(page, pageSize));

        List<PlateTypeEntity> resultList = query.list(pte);
        return new PlateTypes(count, page, pageSize, mapPTE(resultList));
    }

    @Override
    @Transactional
    public PlateType create(PlateType type) {
        type.setId(null);
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
        if (pte.isInUse()) throw new PersistenceException("Plate cannot be deleted while in use.");
        em.remove(pte);
    }

    private List<PlateType> mapPTE(List<PlateTypeEntity> resultList) {
        List<PlateType> list = new ArrayList<>();
        resultList.forEach(pte -> list.add(Mappers.PLATETYPE.map(pte)));
        return list;
    }
    private List<PlateType> map(List<Tuple> tuples) {
        List<PlateType> list = new ArrayList<>();
        for(Tuple tuple : tuples) {
            PlateType pt = Mappers.PLATETYPE.map(tuple.get(QPlateTypeEntity.plateTypeEntity));
            //noinspection ConstantConditions
            pt.setPlateCount(tuple.get(1, Long.class) - 1L);
            list.add(pt);
        }
        return list;
    }

}
