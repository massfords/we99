package edu.harvard.we99.services;

import edu.harvard.we99.domain.Protocol;
import edu.harvard.we99.services.storage.CRUDStorage;

import javax.ws.rs.core.Response;

/**
 * @author mford
 */
public class ProtocolServiceImpl extends BaseRESTServiceImpl<Protocol>  implements ProtocolService {
    public ProtocolServiceImpl(CRUDStorage<Protocol> storage) {
        super(storage);
    }

    @Override
    public Response delete(Long id) {
        deleteImpl(id);
        return Response.ok().build();
    }
}
