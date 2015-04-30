package edu.harvard.we99.services;

import edu.harvard.we99.domain.Protocol;
import edu.harvard.we99.domain.lists.Protocols;
import edu.harvard.we99.services.storage.ProtocolStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

/**
 * @author mford
 */
public class ProtocolServiceImpl extends BaseRESTServiceImpl<Protocol>  implements ProtocolService {

    private static final Logger log = LoggerFactory.getLogger(ProtocolServiceImpl.class);

    public ProtocolServiceImpl(ProtocolStorage storage) {
        super(storage);
    }

    @Override
    public Response delete(Long id) {
        deleteImpl(id);
        return Response.ok().build();
    }

    @Override
    public Protocols listAll(Integer page, Integer pageSize, String typeAhead) {
        try {
            ProtocolStorage ps = (ProtocolStorage) storage;
            return ps.listAll(page, pageSize, typeAhead);
        } catch(Exception e) {
            log.error("error listing protocols. Page {}, pageSize {}, query {}",
                    page, pageSize, typeAhead, e);
            throw new WebApplicationException(Response.serverError().build());
        }
    }
}
