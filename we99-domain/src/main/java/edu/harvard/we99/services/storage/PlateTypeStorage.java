package edu.harvard.we99.services.storage;

import edu.harvard.we99.domain.PlateDimension;
import edu.harvard.we99.domain.PlateType;
import edu.harvard.we99.domain.lists.PlateTypes;

/**
 * @author mford
 */
public interface PlateTypeStorage extends CRUDStorage<PlateType> {
    PlateTypes listAll(Integer page);

    /**
     * Find all plates that are greater than or equal to the given dimension
     * @param dim
     */
    PlateTypes findGreaterThanOrEqualTo(PlateDimension dim, Integer page);
}
