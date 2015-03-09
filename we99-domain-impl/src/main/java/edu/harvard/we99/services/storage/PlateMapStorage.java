package edu.harvard.we99.services.storage;

import edu.harvard.we99.domain.PlateMap;

import java.util.List;

/**
 * @author mford
 */
public interface PlateMapStorage extends CRUDStorage<PlateMap> {
    List<PlateMap> listAll();
}
