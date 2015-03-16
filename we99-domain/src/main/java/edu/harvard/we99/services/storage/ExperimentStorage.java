package edu.harvard.we99.services.storage;

import edu.harvard.we99.domain.Experiment;
import edu.harvard.we99.security.User;

import java.util.List;

/**
 * @author mford
 */
public interface ExperimentStorage extends CRUDStorage<Experiment> {
    List<Experiment> listAll(User user);
    List<User> listMembers(Long experimentId);
    void addMember(Long experimentId, Long userId);
    void removeMember(Long experimentId, Long userId);
    void addMembers(Long id, List<Long> userIds);
}
