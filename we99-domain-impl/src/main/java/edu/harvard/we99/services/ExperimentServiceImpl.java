package edu.harvard.we99.services;

import edu.harvard.we99.domain.Experiment;
import edu.harvard.we99.domain.lists.Experiments;
import edu.harvard.we99.security.UserContextProvider;
import edu.harvard.we99.services.experiments.ExperimentResource;
import edu.harvard.we99.services.experiments.ExperimentResourceImpl;
import edu.harvard.we99.services.storage.ExperimentStorage;
import edu.harvard.we99.services.storage.PlateStorage;
import edu.harvard.we99.services.storage.ResultStorage;

/**
 * @author mford
 */
public class ExperimentServiceImpl implements ExperimentService {

    private final UserContextProvider ucp;
    private final ExperimentStorage storage;
    private final PlateStorage plateStorage;
    private final ResultStorage resultStorage;

    public ExperimentServiceImpl(UserContextProvider ucp,
                                 ExperimentStorage storage,
                                 PlateStorage plateStorage,
                                 ResultStorage resultStorage) {
        this.storage = storage;
        this.ucp = ucp;
        this.plateStorage = plateStorage;
        this.resultStorage = resultStorage;
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
        return new ExperimentResourceImpl(id, storage, plateStorage, resultStorage);
    }
}
