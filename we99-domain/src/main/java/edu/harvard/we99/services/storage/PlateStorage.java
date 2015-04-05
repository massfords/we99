package edu.harvard.we99.services.storage;

import edu.harvard.we99.domain.Plate;
import edu.harvard.we99.domain.lists.Plates;

/**
 * @author mford
 */
public interface PlateStorage extends CRUDStorage<Plate> {

    Plates listAll(Long experimentId, Integer page, Integer pageSize,
                   String typeAhead);
}
