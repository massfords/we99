package edu.harvard.we99.services;

import edu.harvard.we99.domain.lists.Users;
import edu.harvard.we99.security.User;
import edu.harvard.we99.services.storage.UserStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;

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

    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

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
        } catch(Exception e) {
            log.error("how could the logged in user not find themselves. Email: {}", email);
            throw new WebApplicationException(Response.status(404).build());
        }
    }

    @Override
    public User update(Long id, User user) {
        try {
            // we don't want them to be able to change the role
            user.setRole(null);
            return storage.update(id, user);
        } catch(Exception e) {
            log.error("error updating user id {} with {}", id, user);
            throw new WebApplicationException(Response.serverError().build());
        }
    }

    @Override
    public Users list(Integer page, Integer pageSize, String typeAhead) {
        try {
            return storage.listAll(page, pageSize, typeAhead);
        } catch(Exception e) {
            log.error("error listing users. Page {}, pagesize {}, query {}",
                    page, pageSize, typeAhead, e);
            throw new WebApplicationException(Response.serverError().build());
        }
    }

    @Override
    public Response removeUser(Long id) {
        try {
            storage.delete(id);
            return Response.ok().build();
        } catch (Exception e) {
            log.error("error removing user {}", id);
            throw new WebApplicationException(Response.serverError().build());
        }
    }
}
