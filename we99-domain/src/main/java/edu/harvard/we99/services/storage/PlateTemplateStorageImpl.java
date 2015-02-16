package edu.harvard.we99.services.storage;

import edu.harvard.we99.domain.PlateTemplate;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;

/**
 * Implementation of the PlanTemplateStorage
 *
 * @author mford
 */
public class PlateTemplateStorageImpl implements PlateTemplateStorage {
    /**
     * This is injected by Spring via the standard JPA annotation
     */
    @PersistenceContext
    private EntityManager em;

    /**
     * Creates a TX and performs the INSERT
     * @param template
     * @return
     */
    @Override @Transactional
    public PlateTemplate create(PlateTemplate template) {
        em.persist(template);
        return template;
    }

    @Override
    public PlateTemplate get(Long id) {
        return getOrThrow(id);
    }

    /**
     * Creates a TX and performs the UPDATE
     * @param id
     * @param template
     * @return
     */
    @Override @Transactional
    public PlateTemplate update(Long id, PlateTemplate template) {
        PlateTemplate pt = getOrThrow(id);
        pt.setName(template.getName());
        pt.setDescription(pt.getDescription());

        return em.merge(template.withId(id));
    }

    /**
     * Creates a TX and performs the DELETE
     *
     * @param id
     */
    @Override @Transactional
    public void delete(Long id) {
        PlateTemplate pt = getOrThrow(id);
        em.remove(pt);
    }

    /**
     * Helper method that gets the entity or throws if not found
     * @param id
     * @return
     */
    private PlateTemplate getOrThrow(Long id) {
        PlateTemplate pt = em.find(PlateTemplate.class, id);
        if (pt == null) throw new EntityNotFoundException("No such entity:" + id);
        return pt;
    }
}
