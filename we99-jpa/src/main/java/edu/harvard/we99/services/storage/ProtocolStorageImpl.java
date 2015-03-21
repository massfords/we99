package edu.harvard.we99.services.storage;

import edu.harvard.we99.domain.Protocol;
import edu.harvard.we99.domain.lists.Protocols;
import edu.harvard.we99.services.storage.entities.Mappers;
import edu.harvard.we99.services.storage.entities.ProtocolEntity;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;

import static edu.harvard.we99.services.EntityListingSettings.pageSize;
import static edu.harvard.we99.services.EntityListingSettings.pageToFirstResult;

/**
 * @author mford
 */
public class ProtocolStorageImpl implements ProtocolStorage {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Protocols listAll(Integer page) {
        TypedQuery<ProtocolEntity> query = em.createQuery("select p from ProtocolEntity p", ProtocolEntity.class);
        query.setMaxResults(pageSize());
        query.setFirstResult(pageToFirstResult(page));
        List<ProtocolEntity> resultList = query.getResultList();
        List<Protocol> list = new ArrayList<>(resultList.size());
        resultList.forEach(pe->list.add(Mappers.PROTOCOL.map(pe)));
        return new Protocols(count(), page, list);
    }
    private Long count() {
        TypedQuery<Long> q = em.createQuery(
                "select count(e) from ProtocolEntity e", Long.class);
        return q.getSingleResult();
    }

    @Override
    @Transactional
    public Protocol create(Protocol type) {
        assert type.getId() == null;
        ProtocolEntity pe = Mappers.PROTOCOL.mapReverse(type);
        em.persist(pe);
        return Mappers.PROTOCOL.map(pe);
    }

    @Override
    public Protocol get(Long id) throws EntityNotFoundException {
        ProtocolEntity pe = em.find(ProtocolEntity.class, id);
        return Mappers.PROTOCOL.map(pe);
    }

    @Override
    @Transactional
    public Protocol update(Long id, Protocol type) throws EntityNotFoundException {
        ProtocolEntity pe = em.find(ProtocolEntity.class, id);
        Mappers.PROTOCOL.mapReverse(type, pe);
        em.merge(pe);
        return Mappers.PROTOCOL.map(pe);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        ProtocolEntity pe = em.find(ProtocolEntity.class, id);
        em.remove(pe);
    }
}
