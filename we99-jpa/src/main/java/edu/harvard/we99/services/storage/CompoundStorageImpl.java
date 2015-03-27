package edu.harvard.we99.services.storage;

import com.mysema.query.jpa.impl.JPAQuery;
import com.mysema.query.types.ParamExpression;
import com.mysema.query.types.expr.Param;
import edu.harvard.we99.domain.Compound;
import edu.harvard.we99.domain.lists.Compounds;
import edu.harvard.we99.services.storage.entities.CompoundEntity;
import edu.harvard.we99.services.storage.entities.Mappers;
import edu.harvard.we99.services.storage.entities.QCompoundEntity;
import org.apache.commons.lang.StringUtils;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static edu.harvard.we99.services.EntityListingSettings.pageSize;
import static edu.harvard.we99.services.EntityListingSettings.pageToFirstResult;

/**
 * @author mford
 */
public class CompoundStorageImpl implements CompoundStorage {

    /**
     * This is injected by Spring via the standard JPA annotation
     */
    @PersistenceContext
    protected EntityManager em;

    @Override
    public Compounds listAll(Integer page, String queryString) {
        JPAQuery query = new JPAQuery(em);
        QCompoundEntity table = QCompoundEntity.compoundEntity;
        query.from(table);
        if (StringUtils.isNotBlank(queryString)) {
            query.where(table.name.upper().like("%" + queryString.toUpperCase() + "%"));
        }
        query.orderBy(table.name.asc());
        long count = query.count();
        query.limit(pageSize()).offset(pageToFirstResult(page));
        List<CompoundEntity> resultList = query.list(table);
        List<Compound> compounds = new ArrayList<>();
        resultList.forEach(ce->compounds.add(Mappers.COMPOUND.map(ce)));
        return new Compounds(count, pageSize(), compounds);
    }

    @Override
    @Transactional
    public Map<Compound, Long> resolveIds(Set<Compound> compounds) {
        Map<Compound, Long> resolvedMap = new HashMap<>();
        JPAQuery query = new JPAQuery(em);
        ParamExpression<String> param = new Param<>(String.class);
        query.from(QCompoundEntity.compoundEntity).where(QCompoundEntity.compoundEntity.name.eq(param));
        for(Compound c : compounds) {
            query.set(param, c.getName());
            CompoundEntity resolved = query.uniqueResult(QCompoundEntity.compoundEntity);
            if (resolved != null) {
                resolvedMap.put(new Compound(resolved.getName()), resolved.getId());
            } else {
                CompoundEntity ce = new CompoundEntity(null, c.getName());
                em.persist(ce);
                resolved = ce;
            }
            resolvedMap.put(new Compound(resolved.getName()), resolved.getId());
        }
        return resolvedMap;
    }

    @Override
    @Transactional
    public Compound create(Compound type) {
        type.setId(null);
        CompoundEntity entity = Mappers.COMPOUND.mapReverse(type);
        entity.setId(null);
        em.persist(entity);
        return type.withId(entity.getId());
    }

    @Override
    public Compound get(Long id) throws EntityNotFoundException {
        CompoundEntity ce = em.find(CompoundEntity.class, id);
        return Mappers.COMPOUND.map(ce);
    }

    @Override
    @Transactional
    public Compound update(Long id, Compound type) throws EntityNotFoundException {
        CompoundEntity ce = em.find(CompoundEntity.class, id);
        Mappers.COMPOUND.mapReverse(type, ce);
        em.merge(ce);
        return Mappers.COMPOUND.map(ce);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        CompoundEntity ce = em.find(CompoundEntity.class, id);
        em.remove(ce);
    }
}
