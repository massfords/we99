package edu.harvard.we99.services.storage;

import com.mysema.query.jpa.impl.JPAQuery;
import edu.harvard.we99.domain.Coordinate;
import edu.harvard.we99.domain.lists.PlateResultEntries;
import edu.harvard.we99.domain.results.PlateResult;
import edu.harvard.we99.domain.results.PlateResultEntry;
import edu.harvard.we99.domain.results.ResultStatus;
import edu.harvard.we99.services.storage.entities.Mappers;
import edu.harvard.we99.services.storage.entities.PlateEntity;
import edu.harvard.we99.services.storage.entities.PlateResultEntity;
import edu.harvard.we99.services.storage.entities.QExperimentEntity;
import edu.harvard.we99.services.storage.entities.QPlateEntity;
import edu.harvard.we99.services.storage.entities.QPlateResultEntity;
import edu.harvard.we99.services.storage.entities.WellResultsEntity;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.stream.Collectors;

import static edu.harvard.we99.services.EntityListingSettings.pageSize;
import static edu.harvard.we99.services.EntityListingSettings.pageToFirstResult;

/**
 * @author mford
 */
public class ResultStorageImpl implements ResultStorage {

    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional
    public void updateStatus(Long id, Coordinate coordinate, ResultStatus status) {
        PlateResultEntity pr = getPlateResultEntity(id);
        WellResultsEntity wr = pr.getWellResults().get(coordinate);
        wr.setResultStatus(status);
        em.merge(wr);
    }

    @Override
    @Transactional(readOnly = true)
    public PlateResultEntries listAllByExperiment(Long experimentId, Integer page) {

        JPAQuery query = new JPAQuery(em);
        QPlateResultEntity results = QPlateResultEntity.plateResultEntity;
        QPlateEntity plates = QPlateEntity.plateEntity;
        QExperimentEntity exps = QExperimentEntity.experimentEntity;
        query.from(results, plates, exps)
                .where(results.plate.eq(plates)
                        .and(plates.experiment.eq(exps)
                                .and(exps.id.eq(experimentId))));

        long count = query.count();
        query.limit(pageSize()).offset(pageToFirstResult(page));
        List<PlateResultEntity> list = query.list(results);
        List<PlateResultEntry> collect = list.stream().map(pre ->
                new PlateResultEntry(pre.getId(), pre.getCreated(), pre.getLastModified(), pre.getPlate().getName())).collect(Collectors.toList());
        return new PlateResultEntries(count, page, collect);
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
        PlateResultEntity pre = getPlateResultEntity(id);
        return Mappers.PLATERESULT.map(pre);
    }

    @Override
    @Transactional
    public PlateResult update(Long id, PlateResult type) throws EntityNotFoundException {
        PlateResultEntity pre = getPlateResultEntity(id);
        Mappers.PLATERESULT.mapReverse(type, pre);
        updateWells(type, pre);
        em.merge(pre);
        return Mappers.PLATERESULT.map(pre);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        PlateResultEntity pre = getPlateResultEntity(id);
        em.remove(pre);
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

    private PlateResultEntity getPlateResultEntity(Long id) {

        JPAQuery query = new JPAQuery(em);
        query.from(QPlateResultEntity.plateResultEntity)
                .where(QPlateResultEntity.plateResultEntity.plate.id.eq(id));

        return query.uniqueResult(QPlateResultEntity.plateResultEntity);
    }
}
