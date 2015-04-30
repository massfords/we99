package edu.harvard.we99.services;

import edu.harvard.we99.domain.Experiment;
import edu.harvard.we99.domain.lists.Experiments;
import edu.harvard.we99.security.UserContextProvider;
import edu.harvard.we99.services.experiments.ExperimentResource;
import edu.harvard.we99.services.storage.ExperimentStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

/**
 * @author mford
 */
public abstract class ExperimentServiceImpl implements ExperimentService {

    private static final Logger log = LoggerFactory.getLogger(ExperimentServiceImpl.class);

    private final UserContextProvider ucp;
    private final ExperimentStorage storage;

    public ExperimentServiceImpl(UserContextProvider ucp,
                                 ExperimentStorage storage) {
        this.storage = storage;
        this.ucp = ucp;
    }

    @Override
    public Experiment create(Experiment experiment) {
        try {
            experiment.setId(null);
            return storage.create(experiment);
        } catch(Exception e) {
            log.error("error creating experiemnt {}", experiment, e);
            throw new WebApplicationException(Response.serverError().build());
        }
    }

    @Override
    public Experiments listExperiments(Integer page, Integer pageSize, String typeAhead) {
        try {
            return storage.listAll(ucp.get(), page, pageSize, typeAhead);
        } catch(Exception e) {
            log.error("error listing experiments. Page {} pageSize {} query {}",
                    page, pageSize, typeAhead, e);
            throw new WebApplicationException(Response.serverError().build());
        }
    }

    @Override
    public ExperimentResource getExperiment(Long id) {
        try {
            ucp.assertCallerIsMember(id);
            ExperimentResource er = createExperimentResource();
            er.setId(id);
            // prime the pump so the experiment is loaded and available for our security annotations
            er.get();
            return er;
        } catch(WebApplicationException e) {
            throw e;
        } catch (Exception e) {
            log.error("error getting experiemnt {}", id, e);
            throw new WebApplicationException(Response.serverError().build());
        }
    }

    protected abstract ExperimentResource createExperimentResource();
}
