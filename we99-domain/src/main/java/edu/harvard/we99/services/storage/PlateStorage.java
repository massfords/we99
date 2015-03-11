package edu.harvard.we99.services.storage;

import edu.harvard.we99.domain.Plate;

import java.util.List;

/**
 * @author mford
 */
public interface PlateStorage extends CRUDStorage<Plate> {

    List<Plate> listAll(Long experimentId);
}
