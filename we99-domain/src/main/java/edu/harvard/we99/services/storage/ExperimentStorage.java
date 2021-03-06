package edu.harvard.we99.services.storage;

import edu.harvard.we99.domain.Experiment;
import edu.harvard.we99.domain.lists.Experiments;
import edu.harvard.we99.domain.lists.Users;
import edu.harvard.we99.security.User;

import java.util.List;

/**
 * @author mford
 */
public interface ExperimentStorage extends CRUDStorage<Experiment> {
    Experiments listAll(User user, Integer page, Integer pageSize, String typeAhead);
    Users listMembers(Long experimentId);
    void addMember(Long experimentId, Long userId);
    void removeMember(Long experimentId, Long userId);
    void setMembers(Long id, List<Long> userIds);

    Experiment publish(Experiment experiment);
}
