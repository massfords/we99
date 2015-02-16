package edu.harvard.we99.services.storage;

import edu.harvard.we99.domain.PlateType;

import javax.persistence.EntityNotFoundException;

/**
 * Interface for storing PlateType entities
 *
 * @author mford
 */
public interface PlateTypeStorage {
    /**
     * Creates the PlateTYpe
     * @param type
     * @return
     */
    PlateType create(PlateType type);

    /**
     * Gets the PlateType or throws an EntityNotFoundException
     * @param id
     * @return
     */
    PlateType get(Long id) throws EntityNotFoundException;

    /**
     * Updates the PlateType or throws an EntityNotFoundException
     * @param id
     * @param type
     * @return
     */
    PlateType update(Long id, PlateType type) throws EntityNotFoundException;

    /**
     * Deletes the PlateType or throws an EntityNotFoundException
     * @param id
     */
    void delete(Long id);
}
