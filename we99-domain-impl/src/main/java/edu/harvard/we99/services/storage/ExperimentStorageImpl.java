package edu.harvard.we99.services.storage;

import edu.harvard.we99.domain.Experiment;
import edu.harvard.we99.domain.Plate;
import edu.harvard.we99.security.User;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.TypedQuery;
import java.util.List;

/**
 * @author mford
 */
public class ExperimentStorageImpl extends CRUDStorageImpl<Experiment> implements ExperimentStorage {
    public ExperimentStorageImpl() {
        super(Experiment.class);
    }

    @Override
    @Transactional
    public Experiment create(Experiment entity) {
        Experiment experiment = super.create(entity);
        User user = experiment.getUsers().get(0);
        user.addExperiment(experiment);
        em.merge(user);
        return experiment;
    }

    @Override
    protected void updateFromCaller(Experiment fromDb, Experiment fromUser) {
        fromDb.setName(fromUser.getName());
        fromDb.setProtocol(fromUser.getProtocol());
    }

    @Override
    @Transactional
    public Plate addPlate(Long experimentId, Plate plate) {
        Experiment exp = em.find(Experiment.class, experimentId);
        plate.setExperiment(exp);
        em.persist(plate);
        return plate;
    }

    @Override
    @Transactional
    public void removePlate(Long experimentId, Long plateId) {
        Experiment exp = em.find(Experiment.class, experimentId);
        Plate plate = em.find(Plate.class, plateId);
        if (plate.getExperiment() == exp) {
            em.remove(plate);
        }
    }

    @Override
    public List<Experiment> listAll(User user) {
        TypedQuery<Experiment> tq = em.createQuery("select e from Experiment e, User u where u member of e.members and u = :user", Experiment.class);
        tq.setParameter("user", user);
        return tq.getResultList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> listMembers(Long experimentId) {
        Experiment exp = em.find(Experiment.class, experimentId);
        return exp.getUsers();
    }

    @Override
    @Transactional
    public void addMembers(Long experimentId, List<Long> userIds) {
        userIds.stream().forEach(userId -> addMember(experimentId, userId));
    }

    @Override
    @Transactional
    public void addMember(Long experimentId, Long userId) {
        User user = em.find(User.class, userId);
        Experiment experiment = em.find(Experiment.class, experimentId);

        experiment.addUser(user);
        user.addExperiment(experiment);

        em.merge(user);
        em.merge(experiment);
    }

    @Override
    @Transactional
    public void removeMember(Long experimentId, Long userId) {
        User user = em.find(User.class, userId);
        Experiment experiment = em.find(Experiment.class, experimentId);

        experiment.removeUser(user);
        user.removeExperiment(experiment);

        em.merge(user);
        em.merge(experiment);
    }
}
