package edu.harvard.we99.services.storage;

import edu.harvard.we99.domain.PlateTemplate;

import javax.persistence.EntityNotFoundException;

/**
 * Stroage interface for PlanTemplate entities
 *
 * @author mford
 */
public interface PlateTemplateStorage {
    /**
     * Creates a new template
     * @param template
     * @return
     */
    PlateTemplate create(PlateTemplate template);

    /**
     * Gets an existing template or throws an EntityNotFoundException
     * @param id
     * @return
     */
    PlateTemplate get(Long id) throws EntityNotFoundException;

    /**
     * Updates an existing template or throws an EntityNotFoundException
     * @param id
     * @param template
     * @return
     */
    PlateTemplate update(Long id, PlateTemplate template) throws EntityNotFoundException;

    /**
     * Deletes an existing template or throws an EntityNotFoundException
     * @param id
     */
    void delete(Long id) throws EntityNotFoundException;
}
