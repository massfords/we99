package edu.harvard.we99.services.storage;

import edu.harvard.we99.domain.PlateDimension;
import edu.harvard.we99.domain.PlateType;

import java.util.List;

/**
 * @author mford
 */
public interface PlateTypeStorage extends CRUDStorage<PlateType> {
    List<PlateType> listAll();

    /**
     * Find all plates that are greater than or equal to the given dimension
     * @param dim
     */
    List<PlateType> findGreaterThanOrEqualTo(PlateDimension dim);
}
