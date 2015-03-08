package edu.harvard.we99.services.storage;

import edu.harvard.we99.domain.Coordinate;
import edu.harvard.we99.domain.results.PlateResult;
import edu.harvard.we99.domain.results.ResultStatus;
import edu.harvard.we99.domain.results.WellResults;
import org.springframework.transaction.annotation.Transactional;

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
}
