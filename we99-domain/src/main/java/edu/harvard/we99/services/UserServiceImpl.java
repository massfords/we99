package edu.harvard.we99.services;

import edu.harvard.we99.domain.User;
import edu.harvard.we99.services.storage.UserStorage;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

/**
 * Implementation of the user service.
 *
 * Not much here yet
 *
 * @author mford
 */
public class UserServiceImpl implements UserService {
    /**
     * Used to fetch the user entities from the db
     */
    private final UserStorage storage;

    public UserServiceImpl(UserStorage storage) {
        this.storage = storage;
    }

    @Override
    public User whoami(HttpServletRequest request) {
        String email = request.getUserPrincipal().getName();

        try {
            return storage.findByEmail(email);
        } catch(EntityNotFoundException e) {
            throw new WebApplicationException(Response.status(404).build());
        }
    }
}
