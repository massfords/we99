package edu.harvard.we99.services;

import edu.harvard.we99.domain.Protocol;
import edu.harvard.we99.services.storage.ProtocolStorage;

import javax.ws.rs.core.Response;
import java.util.List;

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
    public List<Protocol> listAll() {
        ProtocolStorage ps = (ProtocolStorage) storage;
        return ps.listAll();
    }
}
