package edu.harvard.we99.security;

import edu.harvard.we99.services.storage.UserStorage;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

/**
 * @author mford
 */
public class UserContextProviderImpl implements UserContextProvider {

    private final UserStorage storage;

    public UserContextProviderImpl(UserStorage storage) {
        this.storage = storage;
    }

    @Override
    public User get() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return storage.findByEmail(authentication.getName());
    }

    @Override
    public void assertCallerIsMember(Long experimentId) {
        User user = get();
        if (!user.isMember(experimentId)) {
            throw new WebApplicationException(Response.status(403).build());
        }
    }
}
