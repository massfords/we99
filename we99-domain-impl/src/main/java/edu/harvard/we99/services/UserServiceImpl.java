package edu.harvard.we99.services;

import edu.harvard.we99.security.User;
import edu.harvard.we99.services.storage.UserStorage;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.persistence.EntityNotFoundException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.util.List;

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
    public User whoami() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        try {
            return storage.findByEmail(email);
        } catch(EntityNotFoundException e) {
            throw new WebApplicationException(Response.status(404).build());
        }
    }

    @Override
    public List<User> list() {
        return storage.listAll();
    }

    @Override
    public List<User> find(String query) {
        return storage.find(query);
    }
}
