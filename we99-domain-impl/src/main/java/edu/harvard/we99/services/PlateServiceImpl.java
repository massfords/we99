package edu.harvard.we99.services;

import edu.harvard.we99.domain.Plate;
import edu.harvard.we99.services.storage.CRUDStorage;

/**
 * @author mford
 */
public class PlateServiceImpl extends BaseRESTServiceImpl<Plate> implements PlateService {
    public PlateServiceImpl(CRUDStorage<Plate> storage) {
        super(storage);
    }
}
