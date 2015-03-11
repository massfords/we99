package edu.harvard.we99.services.storage;

import edu.harvard.we99.domain.Compound;

import java.util.List;

/**
 * @author mford
 */
public interface CompoundStorage extends CRUDStorage<Compound> {
    List<Compound> listAll();
}
