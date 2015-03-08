package edu.harvard.we99.domain;

import edu.harvard.we99.security.User;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author mford
 */
public class AuthenticatedUserRule extends TestWatcher {
    private final String email;
    private final JpaSpringFixture fixture;

    public AuthenticatedUserRule(String email, JpaSpringFixture fixture) {
        this.email = email;
        this.fixture = fixture;
    }

    @Override
    public void starting(Description description) {

        User user = fixture.getUserStorage().findByEmail(email);

        List<GrantedAuthority> grantedAuthorities = user.getRole().getPermissions().values()
                .stream()
                .map(p -> new SimpleGrantedAuthority(p.getName()))
                .collect(Collectors.toList());

        SecurityContextHolder.getContext().setAuthentication(
                new TestingAuthenticationToken(
                        new org.springframework.security.core.userdetails.User(email, "pass",
                                grantedAuthorities), null, grantedAuthorities));
    }

    @Override
    public void finished(Description description) {
        SecurityContextHolder.getContext().setAuthentication(null);
    }
}
