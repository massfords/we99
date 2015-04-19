package edu.harvard.we99.services.storage;

import com.mysema.query.jpa.impl.JPAQuery;
import edu.harvard.we99.domain.*;
import edu.harvard.we99.domain.lists.DoseResponseResults;
import edu.harvard.we99.domain.results.DoseResponseResult;
import edu.harvard.we99.domain.results.ResultStatus;
import edu.harvard.we99.services.storage.entities.*;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.*;

import static edu.harvard.we99.services.EntityListingSettings.pageToFirstResult;

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

        Integer i = count.intValue();
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

    @Transactional
    public List<Long> getPlateIds(Long doseResponseId) throws EntityNotFoundException{
        TypedQuery<DoseResponseResultEntity> query = em.createQuery("select dr from DoseResponseResultEntity dr where dr.id=:id",DoseResponseResultEntity.class);
        query.setParameter("id", doseResponseId);
        DoseResponseResultEntity response = query.getResultList().get(0);
        List<ExperimentPoint> epts = response.getExperimentPoints();
        List<Long> plateIds = new ArrayList<>();
        epts.forEach(ep -> plateIds.add(ep.getPlateId()));

        return plateIds;

    }
    @Override
    @Transactional
    public void createAll(Long experimentId) throws EntityNotFoundException {

        TypedQuery<DoseResponseResultEntity> query = em.createQuery("select dr from DoseResponseResultEntity dr where dr.experiment.id=:id",DoseResponseResultEntity.class);
        query.setParameter("id", experimentId);
        List<DoseResponseResultEntity> existing = query.getResultList();
        for( DoseResponseResultEntity dre : existing){
            em.remove(dre);
        }



        //Get all plate ids and wells for the experiment
        TypedQuery<Object[]> query2 = em.createQuery("select distinct pe.id, we  from PlateEntity AS pe JOIN pe.wells as we where pe.experiment.id=:id",Object[].class);
        query2.setParameter("id", experimentId);
        List<Object[]> experiment2 = query2.getResultList();
        experiment2.size();

        //Mapping wellsid to plate Id
        Map<Long,Long> wellIdToPlateId = new HashMap<>();
        List<Long> wellId = new ArrayList<>();

        for( Object[] results : experiment2){
            Long id = (Long) results[0];
            WellEntity we = (WellEntity) results[1];
            wellId.add(we.getId());
            Collections.sort(wellId);
            wellIdToPlateId.put(we.getId(),id);
        }

        //Get DoseEntities from Wells
        //Map the compound Id and DoseEntities

        Map<DoseEntity, Long> dosePlateMap = new HashMap<>();
        Map<Long, Long> dosetoPlate = new HashMap<>();
        Map<Long, List<DoseEntity>> compoundDoseMap = new HashMap<>();

        //Iterate all wellId's
        Iterator it = wellIdToPlateId.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            Long plateId = (Long)pair.getValue();
            Long wellEntityId = (Long) pair.getKey();

            WellEntity wellEntity = em.find(WellEntity.class, wellEntityId);
            Set<DoseEntity> doses = wellEntity.getContents();
            Object[] dosearray = doses.toArray();
            if (dosearray.length > 0){
                DoseEntity d = (DoseEntity) dosearray[0];
                dosePlateMap.put(d, plateId);
                dosetoPlate.put(d.getId(),plateId);

                //create compoud Id to dose list
                Long compoundId = d.getCompound().getId();
                List<DoseEntity> compoundDoseList = compoundDoseMap.get(compoundId);
                if(compoundDoseList == null) {
                    compoundDoseList = new ArrayList<DoseEntity>();
                    compoundDoseList.add(d);
                    compoundDoseMap.put(compoundId,compoundDoseList);
                } else {
                    compoundDoseList.add(d);
                }

            }
            //it.remove(); // avoids a ConcurrentModificationException
        }

        //create DoseResponseResults for each compound
        Iterator cpdit = compoundDoseMap.entrySet().iterator();
        while (cpdit.hasNext()) {
            Map.Entry pair = (Map.Entry)cpdit.next();
            Long compoundId = (Long)pair.getKey();
            List<DoseEntity> delist = (List<DoseEntity>)pair.getValue();

            CompoundEntity ce = em.find(CompoundEntity.class, compoundId);
            ExperimentEntity ee = em.find(ExperimentEntity.class, experimentId);

            List<ExperimentPoint> expt = new ArrayList<>();
            delist.forEach(ent -> {
                Long plateId = dosePlateMap.get(ent);
                expt.add(new ExperimentPoint(plateId, ent.getId()));
            });
            DoseResponseResultEntity dre = new DoseResponseResultEntity()
                    .setCompound(ce)
                    .setExperiment(ee);
            dre.withExperimentPoints(expt);
            dre.setDoses(delist);
            em.persist(dre);

        }

    }

    @Override
    @Transactional
    public DoseResponseResult create(DoseResponseResult dr) {

        DoseResponseResultEntity dre = new DoseResponseResultEntity();



        /*
        drr.setId(null);
        DoseResponseResultEntity entity;
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
        */
        return Mappers.DOSERESPONSES.map(dre);

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
    public DoseResponseResult addFitParameter(Long doseResponseId, FitParameter fitParam) throws EntityNotFoundException {
        DoseResponseResultEntity dre = em.find(DoseResponseResultEntity.class, doseResponseId);
        dre.getFitParameterMap().put(fitParam.getName(), fitParam);
        em.persist(dre);
        return Mappers.DOSERESPONSES.map(dre);
    }

    @Override
    @Transactional
    public DoseResponseResult updateCurveFitPoints(Long doseResponseId, DoseResponseResult type) throws EntityNotFoundException {
        DoseResponseResultEntity dre = em.find(DoseResponseResultEntity.class, doseResponseId);
        updateCurveFit(type, dre);
        em.merge(dre);
        return Mappers.DOSERESPONSES.map(dre);
    }

    @Override
    @Transactional
    public ExperimentPoint addExperimentPoint(Long doseResponseId, ExperimentPoint type) throws EntityNotFoundException{

        /*
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
//            Long targetId = type.getAssociatedWell().getId();
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
        */
        return new ExperimentPoint();
    }

    @Override
    @Transactional
    public void updateAllExperimentPoints(Long doseResponseId,List<ExperimentPoint> experimentPointTypes) throws EntityNotFoundException{
          experimentPointTypes.forEach(p -> updateExperimentPoint(doseResponseId,p));
    }

    @Override
    @Transactional
    public ExperimentPoint updateExperimentPoint(Long doseResponseId, ExperimentPoint type) throws EntityNotFoundException{
        /*
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
        */
        return new ExperimentPoint();
    }

    @Transactional
    public void addWell(Long doseResponseId, Long wellId){
        WellEntity we = em.find(WellEntity.class, wellId);
        DoseResponseResultEntity dre = em.find(DoseResponseResultEntity.class, doseResponseId);
        //dre.addWell(wellId, we);
        //em.persist(dre);
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

    private void updateCurveFit(DoseResponseResult type, DoseResponseResultEntity dre){
       /*
        dre.getFitParameterMap().clear();
        dre.getCurveFitPoints().clear();
        type.getCurveFitPoints().forEach(pt -> dre.addCurveFitPoint(Mappers.CURVEFITPOINT.mapReverse(pt)));
        dre.getCurveFitPoints().forEach(em::merge);
        type.getFitParameterMap().values().forEach(dre::addFitParameter);
       */
    }
}
