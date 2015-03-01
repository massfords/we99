package edu.harvard.we99.security;

import edu.harvard.we99.services.storage.UserStorage;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.persistence.PersistenceException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

/**
 * Enables the user to manage user entities.
 *
 * @author mford
 */
public class ManagedUserServiceImpl implements ManageUserService {

    private final UserStorage storage;

    public ManagedUserServiceImpl(UserStorage storage) {
        this.storage = storage;
    }

    @Override
    public Response asignRole(Long id, RoleName roleName) {

        User entity = storage.get(id);

        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        if (entity.getEmail().equals(email)) {
            // we don't let users change their own roles
            throw new WebApplicationException(Response.status(403).build());
        }

        try {
            storage.assignRole(id, roleName);
        } catch(PersistenceException e) {
            throw new WebApplicationException(Response.status(404).build());
        }

        return Response.ok().build();
    }
}
