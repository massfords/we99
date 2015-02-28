package edu.harvard.we99.services;

import edu.harvard.we99.domain.PlateType;
import edu.harvard.we99.services.storage.CRUDStorage;

/**
 * Implementation of the PlateTypeService
 *
 * @author mford
 */
public class PlateTypeServiceImpl extends BaseRESTServiceImpl<PlateType> implements PlateTypeService {

    public PlateTypeServiceImpl(CRUDStorage<PlateType> storage) {
        super(storage);
    }
}