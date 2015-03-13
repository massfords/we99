package edu.harvard.we99.services.experiments;

import edu.harvard.we99.domain.Experiment;
import edu.harvard.we99.domain.lists.PlateResultEntries;
import edu.harvard.we99.services.BaseRESTServiceImpl;
import edu.harvard.we99.services.storage.ExperimentStorage;
import edu.harvard.we99.services.storage.PlateStorage;
import edu.harvard.we99.services.storage.ResultStorage;

import javax.ws.rs.core.Response;

/**
 * @author mford
 */
public class ExperimentResourceImpl extends BaseRESTServiceImpl<Experiment>  implements ExperimentResource {

    private final Long id;
    private final PlateStorage plateStorage;
    private final ResultStorage resultStorage;

    public ExperimentResourceImpl(Long id, ExperimentStorage storage,
                                  PlateStorage plateStorage,
                                  ResultStorage resultStorage) {
        super(storage);
        this.id = id;
        this.plateStorage = plateStorage;
        this.resultStorage = resultStorage;
    }

    @Override
    public Experiment get() {
        return super.get(id);
    }

    @Override
    public Experiment update(Experiment experiment) {
        return super.update(id, experiment);
    }

    @Override
    public Response delete() {
        deleteImpl(id);
        return Response.ok().build();
    }

    @Override
    public MemberResource getMembers() {
        return new MemberResourceImpl(id, (ExperimentStorage) storage);
    }

    @Override
    public PlatesResource getPlates() {
        return new PlatesResourceImpl(id, plateStorage, resultStorage);
    }

    @Override
    public PlateResultEntries listResults() {
        return new PlateResultEntries(resultStorage.listAllByExperiment(id));
    }

}
