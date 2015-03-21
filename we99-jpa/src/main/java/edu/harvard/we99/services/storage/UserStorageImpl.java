package edu.harvard.we99.services.storage;

import edu.harvard.we99.domain.lists.Users;
import edu.harvard.we99.security.RoleName;
import edu.harvard.we99.security.User;
import edu.harvard.we99.services.storage.entities.Mappers;
import edu.harvard.we99.services.storage.entities.RoleEntity;
import edu.harvard.we99.services.storage.entities.UserEntity;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static edu.harvard.we99.services.EntityListingSettings.pageSize;
import static edu.harvard.we99.services.EntityListingSettings.pageToFirstResult;
import static org.apache.commons.lang.StringUtils.isNotBlank;

/**
 * Implmentation of the storage interface.
 *
 * @author mford
 */
public class UserStorageImpl implements UserStorage {

    @PersistenceContext
    private EntityManager em;

    /**
     * used to encode the password. This is the same instance that's configured
     * in the Application Context
     */
    @SuppressWarnings("deprecation")
    private final PasswordEncoder passwordEncoder;

    @SuppressWarnings("deprecation")
    public UserStorageImpl(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public User create(User user) {
        assert user.getId() == null;
        // by default, new users get put into the Scientist role. It's up to
        // someone else to elevate them.
        TypedQuery<RoleEntity> query = em.createQuery("select r from RoleEntity r where r.name=:name", RoleEntity.class);
        query.setParameter("name", RoleName.BuiltIn.Scientist.asName());
        RoleEntity scientistRole = query.getSingleResult();
        UserEntity ue = Mappers.USERS.mapReverse(user);
        ue.setRole(scientistRole);
        em.persist(ue);
        User mappedUser = Mappers.USERS.map(ue);
        // newly created user objects will have their password set to a uuid
        mappedUser.setPassword(ue.getPassword());
        return mappedUser;
    }

    @Override
    @Transactional
    public User get(Long id) throws EntityNotFoundException {
        UserEntity ue = em.find(UserEntity.class, id);
        return Mappers.USERS.map(ue);
    }

    @Override
    @Transactional
    public User update(Long id, User fromUser) throws EntityNotFoundException {
        UserEntity ue = em.find(UserEntity.class, id);
        Mappers.USERS.mapReverse(fromUser, ue);
        String password = fromUser.getPassword();
        if (isNotBlank(password)) {
            if (ue.getPasswordStatus() == UserEntity.PasswordStatus.assigned) {
                String encodedPassword = passwordEncoder.encodePassword(
                        password, ue.getSalt());
                ue.setPassword(encodedPassword);
            }
        }
        em.merge(ue);
        return Mappers.USERS.map(ue);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        UserEntity ue = em.find(UserEntity.class, id);
        em.remove(ue);
    }

    @Override
    public User findByUUID(String uuid, String email) {
        TypedQuery<UserEntity> findByUUID = em.createQuery("select u from UserEntity u where u.password = :uuid and u.email=:email", UserEntity.class);
        findByUUID.setParameter("uuid", uuid);
        findByUUID.setParameter("email", email);

        return Mappers.USERS.map(findByUUID.getSingleResult());
    }

    @Override
    public User findByEmail(String email) {
        UserEntity singleResult = findEntityByEmail(email);
        return Mappers.USERS.map(singleResult);
    }

    @Override
    public Users listAll(Integer page) {
        TypedQuery<UserEntity> findAll = em.createQuery(
                "select u from UserEntity u order by u.lastName, u.firstName", UserEntity.class);
        findAll.setMaxResults(pageSize());
        findAll.setFirstResult(pageToFirstResult(page));
        List<UserEntity> resultList = findAll.getResultList();
        return map(count(), page, resultList);
    }
    private Long count() {
        TypedQuery<Long> q = em.createQuery(
                "select count(ce) from UserEntity ce", Long.class);
        return q.getSingleResult();
    }

    @Override
    public Users find(String query, Integer page) {
        TypedQuery<UserEntity> q = em.createQuery("select u from UserEntity u where " +
                "upper(concat(u.lastName, ' ', u.firstName)) like :query or " +
                "upper(concat(u.firstName, ' ', u.lastName)) like :query or " +
                "upper(u.email) like :query", UserEntity.class);
        q.setMaxResults(pageSize());
        q.setFirstResult(pageToFirstResult(page));
        q.setParameter("query", "%"+query.toUpperCase()+"%");
        return map(countSearch(query), page, q.getResultList());
    }

    private Long countSearch(String query) {
        TypedQuery<Long> q = em.createQuery("select count(u) from UserEntity u where " +
                "upper(concat(u.lastName, ' ', u.firstName)) like :query or " +
                "upper(concat(u.firstName, ' ', u.lastName)) like :query or " +
                "upper(u.email) like :query", Long.class);
        q.setParameter("query", "%"+query.toUpperCase()+"%");
        return q.getSingleResult();
    }

    @Override
    @Transactional
    public String resetPassword(Long id) {
        UserEntity user = em.find(UserEntity.class, id);
        String tmpPassword = UUID.randomUUID().toString();
        user.setPassword(tmpPassword);
        user.setPasswordStatus(UserEntity.PasswordStatus.assigned);
        em.merge(user);
        return tmpPassword;
    }

    @Override
    @Transactional
    public void activate(String uuid, String email, String unsaltedPassword) {
        UserEntity user = findEntityByEmail(email);
        if (user.getPassword().equals(uuid)) {
            String encodedPassword = passwordEncoder.encodePassword(
                    unsaltedPassword, user.getSalt());
            user.setPassword(encodedPassword);
        } else {
            throw new IllegalStateException("user account already activated");
        }
        user.setPasswordStatus(UserEntity.PasswordStatus.assigned);
        em.merge(user);
    }

    @Override
    @Transactional
    public void assignRole(Long id, RoleName roleName) {
        UserEntity entity = em.find(UserEntity.class, id);

        TypedQuery<RoleEntity> query = em.createQuery("select r from RoleEntity r where r.name=:name", RoleEntity.class);
        query.setParameter("name", roleName);
        RoleEntity role = query.getSingleResult();
        entity.setRole(role);
        em.merge(entity);
    }

    private Users map(Long count, int page, List<UserEntity> resultList) {
        List<User> list = new ArrayList<>(resultList.size());
        resultList.forEach(ue->list.add(Mappers.USERS.map(ue)));
        return new Users(count, page, list);
    }

    private UserEntity findEntityByEmail(String email) {
        TypedQuery<UserEntity> query = em.createQuery("select u from UserEntity u where u.email=:email", UserEntity.class);
        query.setParameter("email", email);
        return query.getSingleResult();
    }
}
