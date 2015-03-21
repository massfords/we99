package edu.harvard.we99.services.storage;

import edu.harvard.we99.domain.PlateMap;
import edu.harvard.we99.domain.WellMap;
import edu.harvard.we99.domain.lists.PlateMaps;
import edu.harvard.we99.services.storage.entities.LabelEntity;
import edu.harvard.we99.services.storage.entities.Mappers;
import edu.harvard.we99.services.storage.entities.PlateMapEntity;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static edu.harvard.we99.services.EntityListingSettings.pageSize;
import static edu.harvard.we99.services.EntityListingSettings.pageToFirstResult;

/**
 * Implementation of the PlanTemplateStorage
 *
 * @author mford
 */
public class PlateMapStorageImpl implements PlateMapStorage {

    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional(readOnly = true)
    public PlateMaps listAll(Integer page) {
        TypedQuery<PlateMapEntity> query = em.createQuery(
                "select pm from PlateMapEntity pm", PlateMapEntity.class);
        query.setFirstResult(pageToFirstResult(page));
        query.setMaxResults(pageSize());
        List<PlateMap> list = new ArrayList<>();
        List<PlateMapEntity> resultList = query.getResultList();
        resultList.forEach(pme -> list.add(Mappers.PLATEMAP.map(pme)));
        return new PlateMaps(count(), page, list);
    }

    private Long count() {
        TypedQuery<Long> q = em.createQuery(
                "select count(e) from PlateMapEntity e", Long.class);
        return q.getSingleResult();
    }

    @Override
    @Transactional
    public PlateMap create(PlateMap type) {
        assert type.getId() == null;
        PlateMapEntity pme = Mappers.PLATEMAP.mapReverse(type);
        mapWellsManually(type, pme);
        em.persist(pme);
        return Mappers.PLATEMAP.map(pme);
    }

    @Override
    @Transactional(readOnly = true)
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
        type.getWells().values().forEach(wm -> pme.add(Mappers.WELLMAP.mapReverse(wm)));
        pme.getWells().values().forEach(we->we.getLabels().clear());
        for(WellMap wm : type.getWells().values()) {
            List<LabelEntity> labels = wm.getLabels().stream().map(Mappers.LABEL::mapReverse).collect(Collectors.toList());
            labels.forEach(lbl->lbl.setId(null));
            labels.forEach(em::merge);
            pme.getWells().get(wm.getCoordinate()).withLabels(labels);
        }

        pme.getWells().values().forEach(em::merge);
    }
}
