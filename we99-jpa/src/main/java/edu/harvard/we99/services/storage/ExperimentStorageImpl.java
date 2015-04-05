package edu.harvard.we99.services.storage;

import com.mysema.query.jpa.impl.JPAQuery;
import edu.harvard.we99.domain.Experiment;
import edu.harvard.we99.domain.ExperimentStatus;
import edu.harvard.we99.domain.lists.Experiments;
import edu.harvard.we99.domain.lists.Users;
import edu.harvard.we99.security.User;
import edu.harvard.we99.security.UserContextProvider;
import edu.harvard.we99.services.storage.entities.ExperimentEntity;
import edu.harvard.we99.services.storage.entities.Mappers;
import edu.harvard.we99.services.storage.entities.ProtocolEntity;
import edu.harvard.we99.services.storage.entities.QExperimentEntity;
import edu.harvard.we99.services.storage.entities.QUserEntity;
import edu.harvard.we99.services.storage.entities.UserEntity;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static edu.harvard.we99.services.EntityListingSettings.pageToFirstResult;
import static edu.harvard.we99.services.storage.TypeAheadLike.applyTypeAhead;

/**
 * @author mford
 */
public class ExperimentStorageImpl implements ExperimentStorage {
    @PersistenceContext
    private EntityManager em;

    private final UserContextProvider ucp;

    public ExperimentStorageImpl(UserContextProvider ucp) {
        this.ucp = ucp;
    }


    @Override
    @Transactional
    public Experiment create(Experiment xp) {
        xp.setId(null);
        ExperimentEntity entity = Mappers.EXPERIMENTS.mapReverse(xp);

        updateProtocol(xp, entity);

        User user = ucp.get();

        UserEntity ue = em.find(UserEntity.class, user.getId());
        entity.addUser(ue);
        em.persist(entity);
        ue.addExperiment(entity);
        em.merge(ue);
        return Mappers.EXPERIMENTS.map(entity);
    }

    @Override
    public Experiment get(Long id) throws EntityNotFoundException {
        ExperimentEntity ee = em.find(ExperimentEntity.class, id);
        return Mappers.EXPERIMENTS.map(ee);
    }

    @Override
    @Transactional
    public Experiment update(Long id, Experiment type) throws EntityNotFoundException {
        ExperimentEntity ee = em.find(ExperimentEntity.class, id);
        Mappers.EXPERIMENTS.mapReverse(type, ee);
        updateProtocol(type, ee);
        em.merge(ee);
        return Mappers.EXPERIMENTS.map(ee);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        ExperimentEntity ee = em.find(ExperimentEntity.class, id);
        em.remove(ee);
    }

    @Override
    public Experiments listAll(User user, Integer page, Integer pageSize, String typeAhead) {
        JPAQuery query = new JPAQuery(em);
        QExperimentEntity exp = QExperimentEntity.experimentEntity;
        QUserEntity ue = QUserEntity.userEntity;
        query.from(exp, ue);
        query.where(exp.status.eq(ExperimentStatus.PUBLISHED).or(
                exp.members.containsValue(ue)
                        .and(ue.email.eq(user.getEmail()))));
        applyTypeAhead(query, exp.name, typeAhead);
        query.distinct();

        long count = query.count();
        query.limit(pageSize).offset(pageToFirstResult(page, pageSize));
        List<ExperimentEntity> resultList = query.list(exp);
        List<Experiment> experiments = new ArrayList<>();
        resultList.forEach(ee -> experiments.add(Mappers.EXPERIMENTS.map(ee)));
        return new Experiments(count, page, pageSize, experiments);
    }

    @Override
    @Transactional(readOnly = true)
    public Users listMembers(Long experimentId) {
        ExperimentEntity ee = em.find(ExperimentEntity.class, experimentId);
        Collection<UserEntity> values = ee.getMembers().values();
        List<User> userList = new ArrayList<>(values.size());
        values.forEach(u -> userList.add(Mappers.USERS.map(u)));
        return new Users(userList.size(), userList.size(), userList.size(), userList);
    }

    @Override
    @Transactional
    public void addMembers(Long experimentId, List<Long> userIds) {
        userIds.stream().forEach(userId -> addMember(experimentId, userId));
    }

    @Override
    @Transactional
    public Experiment publish(Experiment experiment) {
        ExperimentEntity ee = em.find(ExperimentEntity.class, experiment.getId());
        if (ee == null) {
            throw new EntityNotFoundException();
        }
        ee.setStatus(ExperimentStatus.PUBLISHED);
        em.merge(ee);
        return Mappers.EXPERIMENTS.map(ee);
    }

    @Override
    @Transactional
    public void addMember(Long experimentId, Long userId) {
        UserEntity ue = em.find(UserEntity.class, userId);
        ExperimentEntity ee = em.find(ExperimentEntity.class, experimentId);
        ee.addUser(ue);
        ue.addExperiment(ee);
        em.merge(ue);
        em.merge(ee);
    }

    @Override
    @Transactional
    public void removeMember(Long experimentId, Long userId) {
        UserEntity ue = em.find(UserEntity.class, userId);
        ExperimentEntity ee = em.find(ExperimentEntity.class, experimentId);
        ee.removeUser(ue);
        ue.removeExperiment(ee);
        em.merge(ue);
        em.merge(ee);
    }

    private void updateProtocol(Experiment type, ExperimentEntity ee) {
        Long pid = type.getProtocol().getId();
        if (ee.getProtocol() == null || !ee.getProtocol().getId().equals(pid)) {
            if (pid != null) {
                ProtocolEntity protocol = em.find(ProtocolEntity.class, pid);
                ee.setProtocol(protocol);
            } else {
                ProtocolEntity pe = new ProtocolEntity(type.getProtocol().getName());
                em.persist(pe);
                ee.setProtocol(pe);
            }
        }
    }

}
