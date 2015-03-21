package edu.harvard.we99.services.storage;

import edu.harvard.we99.domain.Experiment;
import edu.harvard.we99.domain.lists.Experiments;
import edu.harvard.we99.domain.lists.Users;
import edu.harvard.we99.security.User;
import edu.harvard.we99.security.UserContextProvider;
import edu.harvard.we99.services.storage.entities.ExperimentEntity;
import edu.harvard.we99.services.storage.entities.Mappers;
import edu.harvard.we99.services.storage.entities.UserEntity;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static edu.harvard.we99.services.EntityListingSettings.pageSize;
import static edu.harvard.we99.services.EntityListingSettings.pageToFirstResult;

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
        assert xp.getId()==null;
        ExperimentEntity entity = Mappers.EXPERIMENTS.mapReverse(xp);
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
    public Experiments listAll(User user, Integer page) {
        TypedQuery<ExperimentEntity> tq = em.createQuery(
                "select e from ExperimentEntity e, UserEntity u " +
                        "where u member of e.members and u.email = :user",
                ExperimentEntity.class);
        tq.setFirstResult(pageToFirstResult(page));
        tq.setMaxResults(pageSize());
        tq.setParameter("user", user.getEmail());
        List<ExperimentEntity> resultList = tq.getResultList();
        List<Experiment> experiments = new ArrayList<>();
        resultList.forEach(ee->experiments.add(Mappers.EXPERIMENTS.map(ee)));
        return new Experiments(count(user.getEmail()), page, experiments);
    }

    private Long count(String email) {
        TypedQuery<Long> tq = em.createQuery(
                "select count(e) from ExperimentEntity e, UserEntity u " +
                        "where u member of e.members and u.email = :user",
                Long.class);
        tq.setParameter("user", email);
        return tq.getSingleResult();
    }

    @Override
    @Transactional(readOnly = true)
    public Users listMembers(Long experimentId) {
        ExperimentEntity ee = em.find(ExperimentEntity.class, experimentId);
        Collection<UserEntity> values = ee.getMembers().values();
        List<User> userList = new ArrayList<>(values.size());
        values.forEach(u->userList.add(Mappers.USERS.map(u)));
        return new Users(userList.size(), userList.size(), userList);
    }

    @Override
    @Transactional
    public void addMembers(Long experimentId, List<Long> userIds) {
        userIds.stream().forEach(userId -> addMember(experimentId, userId));
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
}
