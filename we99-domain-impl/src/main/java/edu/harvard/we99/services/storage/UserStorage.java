package edu.harvard.we99.services.storage;

import edu.harvard.we99.domain.User;

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

    /**
     * Activates the user account by setting the password in place.
     * @param uuid
     * @param email
     * @param password
     * @return
     */
    void activate(String uuid, String email, String password);

}
