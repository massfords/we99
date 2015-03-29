package edu.harvard.we99.services.storage;

import com.mysema.query.jpa.impl.JPAQuery;
import edu.harvard.we99.domain.Protocol;
import edu.harvard.we99.domain.lists.Protocols;
import edu.harvard.we99.services.storage.entities.Mappers;
import edu.harvard.we99.services.storage.entities.ProtocolEntity;
import edu.harvard.we99.services.storage.entities.QProtocolEntity;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
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

        JPAQuery query = new JPAQuery(em);
        query.from(QProtocolEntity.protocolEntity);

        long count = query.count();

        query.limit(pageSize()).offset(pageToFirstResult(page));
        List<ProtocolEntity> resultList = query.list(QProtocolEntity.protocolEntity);
        List<Protocol> list = new ArrayList<>(resultList.size());
        resultList.forEach(pe->list.add(Mappers.PROTOCOL.map(pe)));
        return new Protocols(count, page, pageSize(), list);
    }

    @Override
    @Transactional
    public Protocol create(Protocol type) {
        type.setId(null);
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
