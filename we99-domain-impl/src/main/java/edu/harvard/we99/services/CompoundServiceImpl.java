package edu.harvard.we99.services;

import edu.harvard.we99.domain.Compound;
import edu.harvard.we99.services.storage.CRUDStorage;

import javax.ws.rs.core.Response;

/**
 * Handles the basic CRUD operations for Compounds
 *
 * @author mford
 */
public class CompoundServiceImpl extends BaseRESTServiceImpl<Compound>  implements CompoundService {

    public CompoundServiceImpl(CRUDStorage<Compound> storage) {
        super(storage);
    }

    @Override
    public Response delete(Long id) {
        deleteImpl(id);
        return Response.ok().build();
    }
}
