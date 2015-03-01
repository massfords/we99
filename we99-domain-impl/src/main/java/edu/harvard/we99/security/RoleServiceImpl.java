package edu.harvard.we99.security;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Impl of the RoleService to get the listing of available roles.
 *
 * @author mford
 */
public class RoleServiceImpl implements RoleService {
    @PersistenceContext
    private EntityManager em;

    @Override
    public List<RoleName> getRoleNames() {
        List<Role> roles = em.createQuery(
                "select r from Role r", Role.class).getResultList();

        return roles.stream().map(Role::getName).collect(Collectors.toList());
    }
}
