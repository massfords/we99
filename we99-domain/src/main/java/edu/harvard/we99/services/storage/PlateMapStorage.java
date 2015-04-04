package edu.harvard.we99.services.storage;

import edu.harvard.we99.domain.PlateDimension;
import edu.harvard.we99.domain.PlateMap;
import edu.harvard.we99.domain.lists.PlateMaps;

/**
 * @author mford
 */
public interface PlateMapStorage extends CRUDStorage<PlateMap> {
    PlateMaps listAll(Integer page, Integer pageSize, PlateDimension maxDim);
}
