package edu.harvard.we99.security;

import edu.harvard.we99.services.storage.entities.Mappers;
import edu.harvard.we99.services.storage.entities.UserEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

/**
 * Custom spring UserDetailsService that uses JPA to fetch the User entities
 * as part of the authentication/authorization framework.
 *
 * @author mford
 */
public class CustomUserDetailsService implements UserDetailsService {

    private static final Logger log = LoggerFactory.getLogger(CustomUserDetailsService.class);

    /**
     * This is injected by spring
     */
    @PersistenceContext
    private EntityManager em;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            TypedQuery<UserEntity> query = em.createQuery(
                    "select u from UserEntity as u where u.email = :email", UserEntity.class);
            query.setParameter("email", username);
            UserEntity u = query.getSingleResult();

            SaltyUser user = new SaltyUser(u);
            return user;
        } catch (Exception e) {
            log.error("error resolving user with username {}", username);
            throw new UsernameNotFoundException(username);
        }
    }
}
