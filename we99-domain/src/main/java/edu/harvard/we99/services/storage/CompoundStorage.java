package edu.harvard.we99.services.storage;

import edu.harvard.we99.domain.Compound;
import edu.harvard.we99.domain.lists.Compounds;

/**
 * @author mford
 */
public interface CompoundStorage extends CRUDStorage<Compound> {
    Compounds listAll(Integer page);
}
