package edu.harvard.we99.services.storage;

import edu.harvard.we99.domain.results.PlateResult;

/**
 * @author mford
 */
public class ResultStorageImpl extends CRUDStorageImpl<PlateResult> implements ResultStorage {
    public ResultStorageImpl() {
        super(PlateResult.class);
    }

    @Override
    protected void updateFromCaller(PlateResult fromDb, PlateResult fromUser) {
        // todo -- need to implement
    }
}
