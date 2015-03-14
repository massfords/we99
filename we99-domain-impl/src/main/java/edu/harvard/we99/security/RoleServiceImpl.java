package edu.harvard.we99.security;

import edu.harvard.we99.services.storage.entities.RoleEntity;

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
        List<RoleEntity> roles = em.createQuery(
                "select r from RoleEntity r", RoleEntity.class).getResultList();

        return roles.stream().map(RoleEntity::getName).collect(Collectors.toList());
    }
}
