package edu.harvard.we99.services.storage;

import edu.harvard.we99.security.RoleName;
import edu.harvard.we99.security.User;

import java.util.List;

/**
 * Storage interface for reading/writing User objects to the system.
 *
 * @author mford
 */
public interface UserStorage extends CRUDStorage<User> {

    /**
     * Looks up a user that has the given UUID for their
     * password. This is only for users that are in the
     * process of creating a new account.
     * @param uuid
     * @param email
     * @return User or EntityNotFoundException
     */
    User findByUUID(String uuid, String email);

    /**
     * Find the user by their email
     * @param email
     * @return
     */
    User findByEmail(String email);

    List<User> listAll();

    /**
     * Returns all of the users that match the given query. The query is considered
     * as literal text that appears in their first name, last name, or email address
     * @param query
     * @return
     */
    List<User> find(String query);

    /**
     * Activates the user account by setting the password in place.
     * @param uuid
     * @param email
     * @param password
     * @return
     */
    void activate(String uuid, String email, String password);

    void assignRole(Long id, RoleName roleName);

}
