package edu.harvard.we99.services;

import edu.harvard.we99.domain.Experiment;
import edu.harvard.we99.domain.lists.Experiments;
import edu.harvard.we99.domain.lists.Users;
import edu.harvard.we99.security.User;
import edu.harvard.we99.security.UserContextProvider;
import edu.harvard.we99.services.storage.ExperimentStorage;

import javax.ws.rs.core.Response;
import java.util.List;

/**
 * @author mford
 */
public class ExperimentServiceImpl extends BaseRESTServiceImpl<Experiment>  implements ExperimentService {

    private UserContextProvider ucp;

    public ExperimentServiceImpl(UserContextProvider ucp, ExperimentStorage storage) {
        super(storage);
        this.ucp = ucp;
    }

    @Override
    public Experiment create(Experiment type) {
        User user = ucp.get();
        type.addUser(user);
        return super.create(type);
    }

    @Override
    public Experiment get(Long id) {
        ucp.assertCallerIsMember(id);
        return super.get(id);
    }

    @Override
    public Experiment update(Long id, Experiment plateMap) {
        ucp.assertCallerIsMember(id);
        return super.update(id, plateMap);
    }

    @Override
    public Response delete(Long id) {
        ucp.assertCallerIsMember(id);
        deleteImpl(id);
        return Response.ok().build();
    }

    @Override
    public Experiments listExperiments() {
        return new Experiments(storage().listAll(ucp.get()));
    }

    @Override
    public Users listMembers(Long id) {
        ucp.assertCallerIsMember(id);
        return new Users(storage().listMembers(id));
    }

    @Override
    public Response setMembers(Long id, List<Long> userIds) {
        ucp.assertCallerIsMember(id);
        storage().addMembers(id, userIds);
        return Response.ok().build();
    }

    @Override
    public Response addMember(Long id, Long userId) {
        ucp.assertCallerIsMember(id);
        storage().addMember(id, userId);
        return Response.ok().build();
    }

    @Override
    public Response removeMember(Long id, Long userId) {
        ucp.assertCallerIsMember(id);
        storage().removeMember(id, userId);
        return Response.ok().build();
    }

    private ExperimentStorage storage() {
        return (ExperimentStorage) storage;
    }
}
