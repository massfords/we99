package edu.harvard.we99.services.storage;

import edu.harvard.we99.domain.Experiment;
import edu.harvard.we99.domain.Plate;
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
    protected void updateFromCaller(Experiment fromDb, Experiment fromUser) {
        // todo - need to impl
    }

    @Override
    @Transactional
    public Plate addPlate(Long experimentId, Plate plate) {
        Experiment exp = em.find(Experiment.class, experimentId);
        exp.getPlates().add(plate);
        em.persist(plate);
        em.merge(exp);
        return plate;
    }

    @Override
    @Transactional
    public void removePlate(Long experimentId, Long plateId) {
        Experiment exp = em.find(Experiment.class, experimentId);
        for(Plate p : exp.getPlates()) {
            if (p.getId().equals(plateId)) {
                exp.getPlates().remove(p);
                em.remove(p);
                break;
            }
        }
    }

    @Override
    public List<Experiment> listAll() {
        TypedQuery<Experiment> tq = em.createQuery("select e from Experiment e", Experiment.class);
        return tq.getResultList();
    }
}
