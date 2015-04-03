package edu.harvard.we99.domain;

import edu.harvard.we99.security.AuthenticatedContext;
import edu.harvard.we99.security.User;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

/**
 * @author mford
 */
public class AuthenticatedUserRule extends TestWatcher {
    private final String email;
    private final JpaSpringFixture fixture;
    private AuthenticatedContext context;

    public AuthenticatedUserRule(String email, JpaSpringFixture fixture) {
        this.email = email;
        this.fixture = fixture;
    }

    @Override
    public void starting(Description description) {
        User user = fixture.getUserStorage().findByEmail(email);
        context = new AuthenticatedContext();
        context.install(user);
    }

    @Override
    public void finished(Description description) {
        context.close();
    }
}
