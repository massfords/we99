package edu.harvard.we99.services.storage;

import edu.harvard.we99.domain.Protocol;
import edu.harvard.we99.domain.lists.Protocols;

/**
 * @author mford
 */
public interface ProtocolStorage extends CRUDStorage<Protocol> {
    Protocols listAll(Integer page, Integer pageSize);
}
