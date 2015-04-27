package edu.harvard.we99.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Used in places where we need to operate outside of an established security
 * context. This would be either at startup where we init the db or eventually
 * elsewhere if we have any async/scheduled operations
 *
 * Implements AutoCloseable to make it easy to close at the end of the scope
 * where it's used.
 *
 * @author markford
 */
public class AuthenticatedContext implements AutoCloseable {
    public void install(User user) {
        List<GrantedAuthority> grantedAuthorities = user.getRole().getPermissions().values()
                .stream()
                .map(p -> new SimpleGrantedAuthority(p.getName()))
                .collect(Collectors.toList());

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(
                        new org.springframework.security.core.userdetails.User(user.getEmail(), "pass",
                                grantedAuthorities), null, grantedAuthorities));
    }

    public void close() {
        SecurityContextHolder.getContext().setAuthentication(null);
    }
}
