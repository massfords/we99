package edu.harvard.we99.services.storage;

import com.mysema.query.jpa.impl.JPAQuery;
import edu.harvard.we99.domain.Coordinate;
import edu.harvard.we99.domain.ExperimentPoint;
import edu.harvard.we99.domain.lists.DoseResponseResults;
import edu.harvard.we99.domain.results.DoseResponseResult;
import edu.harvard.we99.domain.results.ResultStatus;
import edu.harvard.we99.services.storage.entities.*;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

import static edu.harvard.we99.services.EntityListingSettings.pageToFirstResult;
import static edu.harvard.we99.services.storage.TypeAheadLike.applyTypeAhead;

/**
 * @author alan orcharton
 */
public class DoseResponseResultStorageImpl implements DoseResponseResultStorage {

    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional
    public DoseResponseResults getAll(Long experimentId){
        JPAQuery query = new JPAQuery(em);
        query.from(QDoseResponseResultEntity.doseResponseResultEntity)
                .where(QDoseResponseResultEntity.doseResponseResultEntity.experiment.id.eq(experimentId));
        Long count = query.count();

        List<DoseResponseResultEntity> resultList = query.list(QDoseResponseResultEntity.doseResponseResultEntity);
        List<DoseResponseResult> list = new ArrayList<>(resultList.size());
        resultList.forEach(drre -> list.add(Mappers.DOSERESPONSES.map(drre)));

        Integer i = count != null ? count.intValue() : null;
        return new DoseResponseResults(count,0,i,list);

    }

    @Override
    @Transactional(readOnly = true)
    public DoseResponseResults listAll(Long experimentId, Integer page, Integer pageSize, String typeAhead) {

        JPAQuery query = new JPAQuery(em);
        query.from(QDoseResponseResultEntity.doseResponseResultEntity)
                .where(QDoseResponseResultEntity.doseResponseResultEntity.experiment.id.eq(experimentId));

        long count = query.count();
        query.limit(pageSize).offset(pageToFirstResult(page, pageSize));

        List<DoseResponseResultEntity> resultList = query.list(QDoseResponseResultEntity.doseResponseResultEntity);
        List<DoseResponseResult> list = new ArrayList<>(resultList.size());
        resultList.forEach(drre -> list.add(Mappers.DOSERESPONSES.map(drre)));

        return new DoseResponseResults(count, page, pageSize, list);
    }

    @Override
    @Transactional
    public void updateStatus(Long id, Coordinate coordinate, ResultStatus status) {
        //DoseResponseResultEntity dr = getDoseResponseResultEntity(id);
        //WellResultsEntity wr = pr.getWellResults().get(coordinate);

    }

    @Override
    @Transactional
    public DoseResponseResult create(DoseResponseResult drr) {
        drr.setId(null);
        DoseResponseResultEntity entity = null;
        JPAQuery query = new JPAQuery(em);
        query.from(QDoseResponseResultEntity.doseResponseResultEntity)
                .where(QDoseResponseResultEntity.doseResponseResultEntity
                        .compound.name.equalsIgnoreCase(drr.getCompound().getName()));
        List<DoseResponseResultEntity> resultList = query.list(QDoseResponseResultEntity.doseResponseResultEntity);
        long count = query.count();
        if(count > 0){
           entity = resultList.get(0);
        } else {
            entity = Mappers.DOSERESPONSES.mapReverse(drr);
            updateCompound(drr, entity);
            em.persist(entity);
        }

        return Mappers.DOSERESPONSES.map(entity);
    }

    @Override
    @Transactional
    public DoseResponseResult get(Long id) throws EntityNotFoundException {
        DoseResponseResultEntity dre = em.find(DoseResponseResultEntity.class, id);
        return Mappers.DOSERESPONSES.map(dre);
    }

    @Override
    @Transactional
    public DoseResponseResult update(Long id, DoseResponseResult type) throws EntityNotFoundException {
        DoseResponseResultEntity dre = em.find(DoseResponseResultEntity.class, id);
        Mappers.DOSERESPONSES.mapReverse(type,dre);
        em.merge(dre);
        return Mappers.DOSERESPONSES.map(dre);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        DoseResponseResultEntity dre = em.find(DoseResponseResultEntity.class,id);
        em.remove(dre);

    }

    @Override
    @Transactional
    public ExperimentPoint addExperimentPoint(Long doseResponseId, ExperimentPoint type) throws EntityNotFoundException{
        PlateEntity pe = em.find(PlateEntity.class, type.getAssociatedPlate().getId());
        WellEntity we = em.find(WellEntity.class, type.getAssociatedWell().getId());
        DoseResponseResultEntity dre = em.find(DoseResponseResultEntity.class, doseResponseId);

        JPAQuery query = new JPAQuery(em);
        QExperimentPointEntity expoints = new QExperimentPointEntity("expoints");
        query.from(QDoseResponseResultEntity.doseResponseResultEntity)
                .join(QDoseResponseResultEntity.doseResponseResultEntity.experimentPoints, expoints)
                .where(expoints.associatedWell.id.eq(type.getAssociatedWell().getId()));


        long count = query.count();
        if (count > 0 ){
            Long targetId = type.getAssociatedWell().getId();
            //ExperimentPointEntity targetPointEntity = em.find(ExperimentPointEntity.class, get(targetId));
            return type;

        }



        ExperimentPointEntity epe = Mappers.EXPERIMENTPOINT.mapReverse(type);
        epe.setAssociatedPlate(pe);
        epe.setAssociatedWell(we);
        epe.setAssociatedDoseResponseResult(dre);
        dre.addExperimentPoint(epe);
        em.persist(epe);

        return Mappers.EXPERIMENTPOINT.map(epe);

    }

    @Override
    @Transactional
    public void updateAllExperimentPoints(Long doseResponseId,List<ExperimentPoint> experimentPointTypes) throws EntityNotFoundException{
          experimentPointTypes.forEach(p -> updateExperimentPoint(doseResponseId,p));
    }

    @Override
    @Transactional
    public ExperimentPoint updateExperimentPoint(Long doseResponseId, ExperimentPoint type) throws EntityNotFoundException{
        PlateEntity pe = em.find(PlateEntity.class, type.getAssociatedPlate().getId());
        WellEntity we = em.find(WellEntity.class, type.getAssociatedWell().getId());
        DoseResponseResultEntity dre = em.find(DoseResponseResultEntity.class, doseResponseId);
        ExperimentPointEntity epe = em.find(ExperimentPointEntity.class, type.getId());
        Mappers.EXPERIMENTPOINT.mapReverse(type,epe);
        epe.setAssociatedWell(we);
        epe.setAssociatedPlate(pe);
        epe.setAssociatedDoseResponseResult(dre);
        em.persist(epe);

        return Mappers.EXPERIMENTPOINT.map(epe);

    }

    @Transactional
    public void addWell(Long doseResponseId, Long wellId){
        WellEntity we = em.find(WellEntity.class, wellId);
        DoseResponseResultEntity dre = em.find(DoseResponseResultEntity.class, doseResponseId);
        dre.addWell(wellId,we);
        em.persist(dre);
    }
    @Transactional
    public void addWells(Long doseResponseId,List<Long> wellsId){
        wellsId.stream().forEach(well -> addWell(doseResponseId, well));
    }

    private void updateCompound(DoseResponseResult type, DoseResponseResultEntity dre) {
        Long pid = type.getCompound().getId();
        if (dre.getCompound() == null || !dre.getCompound().getId().equals(pid)) {
            if (pid != null) {
                CompoundEntity compound = em.find(CompoundEntity.class, pid);
                dre.setCompound(compound);
            } else {
                CompoundEntity ce = new CompoundEntity().setName(type.getCompound().getName());
                em.persist(ce);
                dre.setCompound(ce);
            }
        }
    }


}
