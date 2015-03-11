package edu.harvard.we99.services.storage;

import edu.harvard.we99.domain.Compound;
import edu.harvard.we99.services.storage.entities.CompoundEntity;
import edu.harvard.we99.services.storage.entities.Mappers;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;

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
    public List<Compound> listAll() {
        TypedQuery<CompoundEntity> query = em.createQuery("select c from CompoundEntity c", CompoundEntity.class);
        List<CompoundEntity> resultList = query.getResultList();
        List<Compound> compounds = new ArrayList<>();
        resultList.forEach(ce->compounds.add(Mappers.COMPOUND.map(ce)));
        return compounds;
    }

    @Override
    @Transactional
    public Compound create(Compound type) {
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
