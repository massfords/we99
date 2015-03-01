package edu.harvard.we99.services;

import edu.harvard.we99.domain.PlateType;
import edu.harvard.we99.services.storage.CRUDStorage;

import javax.ws.rs.core.Response;

/**
 * Implementation of the PlateTypeService
 *
 * @author mford
 */
public class PlateTypeServiceImpl extends BaseRESTServiceImpl<PlateType> implements PlateTypeService {

    public PlateTypeServiceImpl(CRUDStorage<PlateType> storage) {
        super(storage);
    }

    @Override
    public Response delete(Long id) {
        deleteImpl(id);
        return Response.ok().build();
    }
}