package edu.harvard.we99.services.storage;

import edu.harvard.we99.domain.BaseEntity;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;

/**
 * @author mford
 */
public abstract class CRUDStorageImpl<T extends BaseEntity> implements CRUDStorage<T> {
    /**
     * This is injected by Spring via the standard JPA annotation
     */
    @PersistenceContext
    protected EntityManager em;

    private final Class<T> clazz;

    public CRUDStorageImpl(Class<T> clazz) {
        this.clazz = clazz;
    }

    /**
     * Creates a TX and performs the INSERT
     * @param entity
     * @return
     */
    @Transactional
    public T create(T entity) {
        em.persist(entity);
        return entity;
    }

    /**
     * Gets the entity with the given pk
     * @param id
     * @return
     */
    public T get(Long id) {
        return getOrThrow(id);
    }

    /**
     * Creates a TX and performs the UPDATE
     * @param id
     * @param entity
     * @return
     */
    @Transactional
    public T update(Long id, T entity) {
        T fromDb = getOrThrow(id);
        updateFromCaller(fromDb, entity);
        entity.setId(id);
        return em.merge(fromDb);
    }

    protected abstract void updateFromCaller(T fromDb, T fromUser);

    /**
     * Creates a TX and performs the DELETE
     *
     * @param id
     */
    @Transactional
    public void delete(Long id) {
        T entity = getOrThrow(id);
        em.remove(entity);
    }

    /**
     * Helper method that gets the entity or throws if not found
     * @param id
     * @return
     */
    private T getOrThrow(Long id) {
        T pt = em.find(clazz, id);
        if (pt == null) throw new EntityNotFoundException("No such entity:" + id);
        return pt;
    }

}
