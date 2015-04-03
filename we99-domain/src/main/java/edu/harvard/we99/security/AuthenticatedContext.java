package edu.harvard.we99.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.stream.Collectors;

/**
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
