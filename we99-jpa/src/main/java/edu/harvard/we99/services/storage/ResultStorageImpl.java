package edu.harvard.we99.services.storage;

import edu.harvard.we99.domain.Coordinate;
import edu.harvard.we99.domain.results.PlateResult;
import edu.harvard.we99.domain.results.PlateResultEntry;
import edu.harvard.we99.domain.results.ResultStatus;
import edu.harvard.we99.services.storage.entities.Mappers;
import edu.harvard.we99.services.storage.entities.PlateEntity;
import edu.harvard.we99.services.storage.entities.PlateResultEntity;
import edu.harvard.we99.services.storage.entities.WellResultsEntity;
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
public class ResultStorageImpl implements ResultStorage {

    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional
    public void updateStatus(Long id, Coordinate coordinate, ResultStatus status) {
        PlateResultEntity pr = em.find(PlateResultEntity.class, id);
        WellResultsEntity wr = pr.getWellResults().get(coordinate);
        wr.setResultStatus(status);
        em.merge(wr);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PlateResult> listAllByPlate(Long experimentId, Long plateId) {
        TypedQuery<PlateResultEntity> query = em.createQuery(
                "select pr from PlateResultEntity pr, PlateEntity p, ExperimentEntity exp " +
                        "where pr.plate = p " +
                        "and p.experiment = exp " +
                        "and exp.id = :expId " +
                        "and p.id=:plateId", PlateResultEntity.class);
        query.setParameter("expId", experimentId);
        query.setParameter("plateId", plateId);
        List<PlateResultEntity> resultList = query.getResultList();
        return map(resultList);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PlateResultEntry> listAllByExperiment(Long experimentId) {
        TypedQuery<PlateResultEntry> query = em.createQuery(
                "select NEW edu.harvard.we99.domain.results.PlateResultEntry(" +
                        "pr.id, pr.created, pr.lastModified, p.name) " +
                        "from PlateResultEntity pr, PlateEntity p, ExperimentEntity exp " +
                        "where pr.plate = p " +
                        "and p.experiment = exp " +
                        "and exp.id = :expId", PlateResultEntry.class);
        query.setParameter("expId", experimentId);
        return query.getResultList();
    }

    @Override
    @Transactional
    public PlateResult create(PlateResult type) {
        assert type.getId() == null;
        PlateResultEntity pre = Mappers.PLATERESULT.mapReverse(type);

        PlateEntity plate = getPlate(type);
        pre.setPlate(plate);

        // need to copy the result wells manually
        updateWells(type, pre);
        em.persist(pre);
        return Mappers.PLATERESULT.map(pre);
    }

    @Override
    @Transactional(readOnly = true)
    public PlateResult get(Long id) throws EntityNotFoundException {
        PlateResultEntity pre = em.find(PlateResultEntity.class, id);
        return Mappers.PLATERESULT.map(pre);
    }

    @Override
    @Transactional
    public PlateResult update(Long id, PlateResult type) throws EntityNotFoundException {
        PlateEntity pe = getPlate(type);
        PlateResultEntity pre = em.find(PlateResultEntity.class, id);
        if (pe != pre.getPlate()) {
            throw new EntityNotFoundException("result not part of experiment/plate");
        }
        Mappers.PLATERESULT.mapReverse(type, pre);
        updateWells(type, pre);
        em.merge(pre);
        return Mappers.PLATERESULT.map(pre);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        PlateResultEntity pre = em.find(PlateResultEntity.class, id);
        em.remove(pre);
    }

    private List<PlateResult> map(List<PlateResultEntity> resultList) {
        List<PlateResult> list = new ArrayList<>(resultList.size());
        resultList.forEach(pre -> list.add(Mappers.PLATERESULT.map(pre)));
        return list;
    }

    private void updateWells(PlateResult type, PlateResultEntity pre) {
        pre.getWellResults().clear();
        type.getWellResults().values().forEach(wr -> pre.add(Mappers.WELLRESULT.mapReverse(wr)) );
        pre.getWellResults().values().forEach(em::merge);
    }

    private PlateEntity getPlate(PlateResult type) {
        PlateEntity plate = em.find(PlateEntity.class, type.getPlate().getId());

        if (!plate.getExperiment().getId().equals(type.getPlate().getExperiment().getId())) {
            String msg = "no plate found with id %d in experiment %d";
            throw new EntityNotFoundException(String.format(msg, type.getPlate().getId(), type.getPlate().getExperiment().getId()));
        }
        return plate;
    }
}
