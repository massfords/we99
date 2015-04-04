package edu.harvard.we99.services;

import edu.harvard.we99.domain.Protocol;
import edu.harvard.we99.domain.lists.Protocols;
import edu.harvard.we99.services.storage.ProtocolStorage;

import javax.ws.rs.core.Response;

/**
 * @author mford
 */
public class ProtocolServiceImpl extends BaseRESTServiceImpl<Protocol>  implements ProtocolService {
    public ProtocolServiceImpl(ProtocolStorage storage) {
        super(storage);
    }

    @Override
    public Response delete(Long id) {
        deleteImpl(id);
        return Response.ok().build();
    }

    @Override
    public Protocols listAll(Integer page, Integer pageSize) {
        ProtocolStorage ps = (ProtocolStorage) storage;
        return ps.listAll(page, pageSize);
    }
}
