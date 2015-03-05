package edu.harvard.we99.services;

import edu.harvard.we99.domain.Experiment;
import edu.harvard.we99.services.storage.ExperimentStorage;

import javax.ws.rs.core.Response;
import java.util.List;

/**
 * @author mford
 */
public class ExperimentServiceImpl extends BaseRESTServiceImpl<Experiment>  implements ExperimentService {
    public ExperimentServiceImpl(ExperimentStorage storage) {
        super(storage);
    }

    @Override
    public Response delete(Long id) {
        deleteImpl(id);
        return Response.ok().build();
    }

    @Override
    public List<Experiment> get() {
        return ((ExperimentStorage)storage).listAll();
    }
}
