package edu.harvard.we99.services.storage;

import edu.harvard.we99.domain.Compound;
import edu.harvard.we99.domain.lists.Compounds;

import java.util.Map;
import java.util.Set;

/**
 * @author mford
 */
public interface CompoundStorage extends CRUDStorage<Compound> {
    Compounds listAll(Integer page);

    Map<Compound, Long> resolveIds(Set<Compound> compounds);
}
