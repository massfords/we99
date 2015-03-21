package edu.harvard.we99.services.storage;

import com.mysema.query.jpa.impl.JPAQuery;
import edu.harvard.we99.domain.Compound;
import edu.harvard.we99.domain.lists.Compounds;
import edu.harvard.we99.services.storage.entities.CompoundEntity;
import edu.harvard.we99.services.storage.entities.Mappers;
import edu.harvard.we99.services.storage.entities.QCompoundEntity;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

import static edu.harvard.we99.services.EntityListingSettings.pageSize;
import static edu.harvard.we99.services.EntityListingSettings.pageToFirstResult;

/**
 * @author mford
 */
public class CompoundStorageImpl implements CompoundStorage {

    /**
     * This is injected by Spring via the standard JPA annotation
     */
    @PersistenceContext
    protected EntityManager em;

    @Override
    public Compounds listAll(Integer page) {

        JPAQuery query = new JPAQuery(em);
        query.from(QCompoundEntity.compoundEntity);
        long count = query.count();
        query.limit(pageSize()).offset(pageToFirstResult(page));
        List<CompoundEntity> resultList = query.list(QCompoundEntity.compoundEntity);
        List<Compound> compounds = new ArrayList<>();
        resultList.forEach(ce->compounds.add(Mappers.COMPOUND.map(ce)));
        return new Compounds(count, pageSize(), compounds);
    }

    @Override
    @Transactional
    public Compound create(Compound type) {
        assert type.getId() == null;
        CompoundEntity entity = Mappers.COMPOUND.mapReverse(type);
        entity.setId(null);
        em.persist(entity);
        return type.withId(entity.getId());
    }

    @Override
    public Compound get(Long id) throws EntityNotFoundException {
        CompoundEntity ce = em.find(CompoundEntity.class, id);
        return Mappers.COMPOUND.map(ce);
    }

    @Override
    @Transactional
    public Compound update(Long id, Compound type) throws EntityNotFoundException {
        CompoundEntity ce = em.find(CompoundEntity.class, id);
        Mappers.COMPOUND.mapReverse(type, ce);
        em.merge(ce);
        return Mappers.COMPOUND.map(ce);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        CompoundEntity ce = em.find(CompoundEntity.class, id);
        em.remove(ce);
    }
}
