package edu.harvard.we99.security;

/**
 * @author mford
 */
public interface UserContextProvider {
    User get();
    void assertCallerIsMember(Long experimentId);
}
