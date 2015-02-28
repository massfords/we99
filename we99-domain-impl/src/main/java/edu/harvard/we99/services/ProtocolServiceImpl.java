package edu.harvard.we99.services;

import edu.harvard.we99.domain.Protocol;
import edu.harvard.we99.services.storage.CRUDStorage;

/**
 * @author mford
 */
public class ProtocolServiceImpl extends BaseRESTServiceImpl<Protocol>  implements ProtocolService {
    public ProtocolServiceImpl(CRUDStorage<Protocol> storage) {
        super(storage);
    }
}
