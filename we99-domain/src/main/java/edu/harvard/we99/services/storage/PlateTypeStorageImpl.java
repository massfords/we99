package edu.harvard.we99.services.storage;

import edu.harvard.we99.domain.PlateType;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;

/**
 * Implementation of the PlateTypeStorage interface
 *
 * @author mford
 */
public class PlateTypeStorageImpl implements PlateTypeStorage {
    /**
     * Injected by Spring via the standard JPA annotation
     */
    @PersistenceContext
    private EntityManager em;

    /**
     * Creates a TX and performs the INSERT
     * @param type
     * @return
     */
    @Override  @Transactional
    public PlateType create(PlateType type) {
        em.persist(type);
        return type;
    }

    @Override
    public PlateType get(Long id) {
        return getOrThrow(id);
    }

    /**
     * Creates a TX and performs the UPDATE
     * @param id
     * @param type
     * @return
     */
    @Override @Transactional
    public PlateType update(Long id, PlateType type) {
        PlateType pt = get(id);
        pt.withManufacturer(type.getManufacturer())
                .withCols(type.getCols())
                .withRows(type.getRows());
        return em.merge(pt);
    }

    /**
     * Creates a TX and performs the DELETE
     * @param id
     */
    @Override @Transactional
    public void delete(Long id) {
        PlateType pt = get(id);
        em.remove(pt);
    }

    /**
     * Helper method for getting the entity or throwing
     *
     * todo - this will get combined via some base class since it's the same as the other storage
     *
     * @param id
     * @return
     */
    private PlateType getOrThrow(Long id) {
        PlateType pt = em.find(PlateType.class, id);
        if (pt == null) throw new EntityNotFoundException("No such entity:" + id);
        return pt;
    }

}
