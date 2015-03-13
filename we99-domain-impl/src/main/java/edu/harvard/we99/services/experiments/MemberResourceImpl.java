package edu.harvard.we99.services.experiments;

import edu.harvard.we99.domain.lists.Users;
import edu.harvard.we99.services.storage.ExperimentStorage;

import javax.ws.rs.core.Response;
import java.util.List;

/**
 * @author mford
 */
public class MemberResourceImpl implements MemberResource {
    private final Long id;
    private final ExperimentStorage storage;

    public MemberResourceImpl(Long id, ExperimentStorage storage) {
        this.id = id;
        this.storage = storage;
    }

    @Override
    public Users listMembers() {
        return new Users(storage.listMembers(id));
    }

    @Override
    public Response setMembers(List<Long> userIds) {
        storage.addMembers(id, userIds);
        return Response.ok().build();
    }

    @Override
    public Response addMember(Long userId) {
        storage.addMember(id, userId);
        return Response.ok().build();
    }

    @Override
    public Response removeMember(Long userId) {
        storage.removeMember(id, userId);
        return Response.ok().build();
    }
}
