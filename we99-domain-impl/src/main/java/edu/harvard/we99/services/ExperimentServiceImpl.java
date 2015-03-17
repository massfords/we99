package edu.harvard.we99.services;

import edu.harvard.we99.domain.Experiment;
import edu.harvard.we99.domain.lists.Experiments;
import edu.harvard.we99.security.UserContextProvider;
import edu.harvard.we99.services.experiments.ExperimentResource;
import edu.harvard.we99.services.storage.ExperimentStorage;

/**
 * @author mford
 */
public abstract class ExperimentServiceImpl implements ExperimentService {

    private final UserContextProvider ucp;
    private final ExperimentStorage storage;

    public ExperimentServiceImpl(UserContextProvider ucp,
                                 ExperimentStorage storage) {
        this.storage = storage;
        this.ucp = ucp;
    }

    @Override
    public Experiment create(Experiment experiment) {
        experiment.setId(null);
        return storage.create(experiment);
    }

    @Override
    public Experiments listExperiments() {
        return new Experiments(storage.listAll(ucp.get()));
    }

    @Override
    public ExperimentResource getExperiment(Long id) {
        ucp.assertCallerIsMember(id);
        ExperimentResource er = createExperimentResource();
        er.setId(id);
        return er;
    }

    protected abstract ExperimentResource createExperimentResource();
}
