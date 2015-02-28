package edu.harvard.we99.services;

import edu.harvard.we99.domain.Plate;
import edu.harvard.we99.services.storage.CRUDStorage;

import javax.ws.rs.core.Response;

/**
 * @author mford
 */
public class PlateServiceImpl extends BaseRESTServiceImpl<Plate> implements PlateService {
    public PlateServiceImpl(CRUDStorage<Plate> storage) {
        super(storage);
    }

    @Override
    public Response delete(Long id) {
        deleteImpl(id);
        return Response.ok().build();
    }
}
