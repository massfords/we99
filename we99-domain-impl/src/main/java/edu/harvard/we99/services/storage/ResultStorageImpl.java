package edu.harvard.we99.services.storage;

import edu.harvard.we99.domain.Coordinate;
import edu.harvard.we99.domain.results.PlateResult;
import edu.harvard.we99.domain.results.PlateResultEntry;
import edu.harvard.we99.domain.results.ResultStatus;
import edu.harvard.we99.domain.results.WellResults;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.TypedQuery;
import java.util.List;

/**
 * @author mford
 */
public class ResultStorageImpl extends CRUDStorageImpl<PlateResult> implements ResultStorage {
    public ResultStorageImpl() {
        super(PlateResult.class);
    }

    @Override
    protected void updateFromCaller(PlateResult fromDb, PlateResult fromUser) {
        fromDb.setComments(fromUser.getComments());
    }

    @Override
    @Transactional
    public void updateStatus(Long id, Coordinate coordinate, ResultStatus status) {
        PlateResult pr = em.find(PlateResult.class, id);
        WellResults wr = pr.getWellResults().get(coordinate);
        wr.setResultStatus(status);
        em.merge(wr);
    }

    @Override
    public List<PlateResult> listAllByPlate(Long experimentId, Long plateId) {
        TypedQuery<PlateResult> query = em.createQuery(
                "select pr from PlateResult pr, Plate p, Experiment exp " +
                        "where pr.plate = p " +
                        "and p.experiment = exp " +
                        "and exp.id = :expId " +
                        "and p.id=:plateId", PlateResult.class);
        query.setParameter("expId", experimentId);
        query.setParameter("plateId", plateId);
        return query.getResultList();
    }

    @Override
    public List<PlateResultEntry> listAllByExperiment(Long experimentId) {
        TypedQuery<PlateResultEntry> query = em.createQuery(
                "select NEW edu.harvard.we99.domain.results.PlateResultEntry(" +
                        "pr.id, pr.created, pr.lastModified, p.name) " +
                        "from PlateResult pr, Plate p, Experiment exp " +
                        "where pr.plate = p " +
                        "and p.experiment = exp " +
                        "and exp.id = :expId", PlateResultEntry.class);
        query.setParameter("expId", experimentId);
        return query.getResultList();
    }
}
