package edu.harvard.we99.services;

import edu.harvard.we99.domain.Compound;
import edu.harvard.we99.domain.lists.Compounds;
import edu.harvard.we99.services.storage.CompoundStorage;

import javax.ws.rs.core.Response;

/**
 * Handles the basic CRUD operations for Compounds
 *
 * @author mford
 */
public class CompoundServiceImpl extends BaseRESTServiceImpl<Compound>  implements CompoundService {

    public CompoundServiceImpl(CompoundStorage storage) {
        super(storage);
    }

    @Override
    public Response delete(Long id) {
        deleteImpl(id);
        return Response.ok().build();
    }

    @Override
    public Compounds listAll(Integer page, Integer pageSize, String queryString) {
        CompoundStorage cs = (CompoundStorage) storage;
        return cs.listAll(page, pageSize, queryString);
    }
}
