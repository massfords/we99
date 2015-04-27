package edu.harvard.we99.security;

/**
 * An attempt at minimizing the spread of spring into the app. This interface is
 * given to services that want to know what User is making the call or whether
 * they have access to a given experiment.
 *
 * @author mford
 */
public interface UserContextProvider {
    User get();
    void assertCallerIsMember(Long experimentId);
}
