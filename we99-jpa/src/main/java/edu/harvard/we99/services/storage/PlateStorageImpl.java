package edu.harvard.we99.services.storage;

import com.mysema.query.jpa.impl.JPAQuery;
import edu.harvard.we99.domain.Plate;
import edu.harvard.we99.domain.lists.Plates;
import edu.harvard.we99.services.storage.entities.ExperimentEntity;
import edu.harvard.we99.services.storage.entities.Mappers;
import edu.harvard.we99.services.storage.entities.PlateEntity;
import edu.harvard.we99.services.storage.entities.PlateTypeEntity;
import edu.harvard.we99.services.storage.entities.QPlateEntity;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.stream.Collectors;

import static edu.harvard.we99.services.EntityListingSettings.pageToFirstResult;
import static edu.harvard.we99.services.storage.TypeAheadLike.applyTypeAhead;

/**
 * @author mford
 */
public class PlateStorageImpl implements PlateStorage {

    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional(readOnly = true)
    public Plates listAll(Long experimentId, Integer page, Integer pageSize, String typeAhead) {

        JPAQuery query = new JPAQuery(em);
        query.from(QPlateEntity.plateEntity)
                .where(QPlateEntity.plateEntity.experiment.id.eq(experimentId));
        applyTypeAhead(query, QPlateEntity.plateEntity.name, typeAhead);
        long count = query.count();
        query.limit(pageSize).offset(pageToFirstResult(page, pageSize));

        List<PlateEntity> resultList = query.list(QPlateEntity.plateEntity);
        List<Plate> list = map(resultList, true);
        return new Plates(count, page, pageSize, list);
    }

    @Transactional(readOnly = true)
    public Plates getAll(Long experimentId) {
        return getPlates(experimentId, true);
    }

    @Transactional(readOnly = true)
    public Plates getAllWithWells(Long experimentId) {
        return getPlates(experimentId, false);
    }

    @Override
    @Transactional
    public Plates create(List<Plate> list) {
        assert !list.isEmpty();
        ExperimentEntity ee = em.find(ExperimentEntity.class, list.get(0).getExperimentId());
        PlateTypeEntity pte = em.find(PlateTypeEntity.class, list.get(0).getPlateType().getId());

        Plates plates = new Plates();

        for(Plate type : list) {
            type.setId(null);
            PlateEntity pe = Mappers.PLATES.mapReverse(type);
            pe.setPlateType(pte);
            pte.addPlate(pe);
            pe.setExperiment(ee);
            updateWells(type, pe);
            em.persist(pe);
            plates.getValues().add(Mappers.PLATES.map(pe));
        }
        em.merge(pte);
        return plates.setPage(0).setPageSize(plates.getValues().size());
    }

    @Override
    @Transactional
    public Plate create(Plate type) {
        type.setId(null);

        ExperimentEntity ee = em.find(ExperimentEntity.class, type.getExperimentId());
        PlateTypeEntity pte = em.find(PlateTypeEntity.class, type.getPlateType().getId());
        PlateEntity pe = Mappers.PLATES.mapReverse(type);
        pe.setPlateType(pte);
        pte.addPlate(pe);
        pe.setExperiment(ee);
        updateWells(type, pe);
        em.persist(pe);
        em.merge(pte);
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
        pe.getExperiment().getPlates().remove(pe);
        PlateTypeEntity pte = pe.getPlateType();
        pte.removePlate(pe);
        em.merge(pte);
        em.merge(pe.getExperiment());
    }

    private void updateWells(Plate type, PlateEntity pe) {
        // need to map the wells manually
        pe.getWells().clear();
        type.getWells().values().forEach(w -> pe.addWell(Mappers.WELL.mapReverse(w)));
        pe.getWells().values().forEach(em::merge);
    }

    private List<Plate> map(List<PlateEntity> resultList, boolean clearWells) {
        List<Plate> list = resultList
                .stream()
                .map(Mappers.PLATES::map)
                .collect(Collectors.toList());
        // don't include the wells in listings
        if (clearWells) {
            list.forEach(p -> p.getWells().clear());
        }
        return list;
    }


    private Plates getPlates(Long experimentId, boolean clearWells) {
        JPAQuery query = new JPAQuery(em);
        query.from(QPlateEntity.plateEntity)
                .where(QPlateEntity.plateEntity.experiment.id.eq(experimentId));
        long count = query.count();
        List<PlateEntity> resultList = query.list(QPlateEntity.plateEntity);
        List<Plate> list = map(resultList, clearWells);
        return new Plates(count,0,list.size(),list);
    }
}
