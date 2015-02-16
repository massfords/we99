package edu.harvard.we99.services.storage;

import edu.harvard.we99.domain.BaseEntity;

import javax.persistence.EntityNotFoundException;

/**
 * Generic interface for storing an entity. This can be expanded as we find we need
 * more methods for accessing the entities.
 *
 * @author mford
 */
public interface CRUDStorage<T extends BaseEntity> {
    /**
     * Creates the entity
     * @param type
     * @return
     */
    T create(T type);

    /**
     * Gets the entity or throws an EntityNotFoundException
     * @param id
     * @return
     */
    T get(Long id) throws EntityNotFoundException;

    /**
     * Updates the entity or throws an EntityNotFoundException
     * @param id
     * @param type
     * @return
     */
    T update(Long id, T type) throws EntityNotFoundException;

    /**
     * Deletes the entity or throws an EntityNotFoundException
     * @param id
     */
    void delete(Long id);
}
