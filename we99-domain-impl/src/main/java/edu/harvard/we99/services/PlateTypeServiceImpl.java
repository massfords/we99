package edu.harvard.we99.services;

import edu.harvard.we99.domain.PlateType;
import edu.harvard.we99.domain.lists.PlateTypes;
import edu.harvard.we99.services.storage.PlateTypeStorage;

import javax.ws.rs.core.Response;

/**
 * Implementation of the PlateTypeService
 *
 * @author mford
 */
public class PlateTypeServiceImpl extends BaseRESTServiceImpl<PlateType> implements PlateTypeService {

    public PlateTypeServiceImpl(PlateTypeStorage storage) {
        super(storage);
    }

    @Override
    public Response delete(Long id) {
        deleteImpl(id);
        return Response.ok().build();
    }

    @Override
    public PlateTypes listAll(Integer page) {
        PlateTypeStorage ps = (PlateTypeStorage) storage;
        return ps.listAll(page);
    }
}