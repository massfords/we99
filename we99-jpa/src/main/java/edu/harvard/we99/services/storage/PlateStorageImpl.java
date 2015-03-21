package edu.harvard.we99.services.storage;

import edu.harvard.we99.domain.Plate;
import edu.harvard.we99.domain.lists.Plates;
import edu.harvard.we99.services.storage.entities.ExperimentEntity;
import edu.harvard.we99.services.storage.entities.Mappers;
import edu.harvard.we99.services.storage.entities.PlateEntity;
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
 * @author mford
 */
public class PlateStorageImpl implements PlateStorage {

    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional(readOnly = true)
    public Plates listAll(Long experimentId, Integer page) {
        TypedQuery<PlateEntity> query = em.createQuery(
                "select p from PlateEntity p where p.experiment.id=:id", PlateEntity.class);
        query.setMaxResults(pageSize());
        query.setFirstResult(pageToFirstResult(page));
        query.setParameter("id", experimentId);
        List<PlateEntity> resultList = query.getResultList();
        List<Plate> list = new ArrayList<>(resultList.size());
        resultList.forEach(pe->list.add(Mappers.PLATES.map(pe)));
        return new Plates(count(), page, list);
    }
    private Long count() {
        TypedQuery<Long> q = em.createQuery(
                "select count(e) from PlateEntity e", Long.class);
        return q.getSingleResult();
    }

    @Override
    @Transactional
    public Plate create(Plate type) {
        assert type.getId() == null;

        ExperimentEntity ee = em.find(ExperimentEntity.class, type.getExperiment().getId());

        PlateEntity pe = Mappers.PLATES.mapReverse(type);
        pe.setExperiment(ee);
        updateWells(type, pe);
        em.persist(pe);
        return Mappers.PLATES.map(pe);
    }

    @Override
    @Transactional(readOnly = true)
    public Plate get(Long id) throws EntityNotFoundException {
        PlateEntity pe = em.find(PlateEntity.class, id);
        return Mappers.PLATES.map(pe);
    }

    @Override
    @Transactional
    public Plate update(Long id, Plate type) throws EntityNotFoundException {
        PlateEntity pe = em.find(PlateEntity.class, id);
        Mappers.PLATES.mapReverse(type, pe);
        updateWells(type, pe);
        em.merge(pe);
        return Mappers.PLATES.map(pe);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        PlateEntity pe = em.find(PlateEntity.class, id);
        em.remove(pe);
    }

    private void updateWells(Plate type, PlateEntity pe) {
        // need to map the wells manually
        pe.getWells().clear();
        type.getWells().values().forEach(w->pe.addWell(Mappers.WELL.mapReverse(w)));
        pe.getWells().values().forEach(em::merge);
    }
}
