package edu.harvard.we99.services.storage;

import edu.harvard.we99.domain.Plate;
import edu.harvard.we99.domain.lists.Plates;

import java.util.List;

/**
 * @author mford
 */
public interface PlateStorage extends CRUDStorage<Plate> {

    Plates listAll(Long experimentId, Integer page, Integer pageSize,
                   String typeAhead);

    Plates getAll(Long experimentId);

    Plates getAllWithWells(Long experimentId);

    Plates create(List<Plate> list);
}
