package edu.harvard.we99.services.storage;

import edu.harvard.we99.domain.Protocol;

import java.util.List;

/**
 * @author mford
 */
public interface ProtocolStorage extends CRUDStorage<Protocol> {
    List<Protocol> listAll();
}
