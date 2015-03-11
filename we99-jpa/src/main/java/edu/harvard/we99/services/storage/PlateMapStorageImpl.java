package edu.harvard.we99.services.storage;

import edu.harvard.we99.domain.PlateMap;
import edu.harvard.we99.services.storage.entities.Mappers;
import edu.harvard.we99.services.storage.entities.PlateMapEntity;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of the PlanTemplateStorage
 *
 * @author mford
 */
public class PlateMapStorageImpl implements PlateMapStorage {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<PlateMap> listAll() {
        TypedQuery<PlateMapEntity> query = em.createQuery("select pm from PlateMapEntity pm", PlateMapEntity.class);
        List<PlateMap> list = new ArrayList<>();
        List<PlateMapEntity> resultList = query.getResultList();
        resultList.forEach(pme -> list.add(Mappers.PLATEMAP.map(pme)));
        return list;
    }

    @Override
    @Transactional
    public PlateMap create(PlateMap type) {
        PlateMapEntity pme = Mappers.PLATEMAP.mapReverse(type);
        mapWellsManually(type, pme);
        em.persist(pme);
        return Mappers.PLATEMAP.map(pme);
    }

    @Override
    public PlateMap get(Long id) throws EntityNotFoundException {
        PlateMapEntity pme = em.find(PlateMapEntity.class, id);
        return Mappers.PLATEMAP.map(pme);
    }

    @Override
    @Transactional
    public PlateMap update(Long id, PlateMap type) throws EntityNotFoundException {
        PlateMapEntity pme = em.find(PlateMapEntity.class, id);
        Mappers.PLATEMAP.mapReverse(type, pme);
        mapWellsManually(type, pme);

        em.merge(pme);
        return Mappers.PLATEMAP.map(pme);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        PlateMapEntity pme = em.find(PlateMapEntity.class, id);
        em.remove(pme);
    }

    private void mapWellsManually(PlateMap type, PlateMapEntity pme) {
        // basic props are set via the mapper, need to map the wells manually
        // because we need to preserve the existing hibernate collections
        pme.getWells().clear();
        type.getWells().values().forEach(wm->pme.add(Mappers.WELLMAP.mapReverse(wm)));
        pme.getWells().values().forEach(em::merge);
    }
}
