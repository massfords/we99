package edu.harvard.we99.services;

import edu.harvard.we99.domain.lists.Users;
import edu.harvard.we99.security.User;
import edu.harvard.we99.services.storage.UserStorage;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.persistence.EntityNotFoundException;
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
    public User whoami() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        try {
            return storage.findByEmail(email);
        } catch(EntityNotFoundException e) {
            throw new WebApplicationException(Response.status(404).build());
        }
    }

    @Override
    public User update(Long id, User user) {
        // we don't want them to be able to change the role
        user.setRole(null);
        return storage.update(id, user);
    }

    @Override
    public Users list(Integer page, Integer pageSize, String typeAhead) {
        return storage.listAll(page, pageSize, typeAhead);
    }

    @Override
    public Response removeUser(Long id) {
        storage.delete(id);
        return Response.ok().build();
    }
}
