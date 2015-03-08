package edu.harvard.we99.services.storage;

import edu.harvard.we99.security.Role;
import edu.harvard.we99.security.RoleName;
import edu.harvard.we99.security.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.TypedQuery;
import java.util.List;
import java.util.UUID;

/**
 * Implmentation of the storage interface.
 *
 * @author mford
 */
public class UserStorageImpl extends CRUDStorageImpl<User> implements UserStorage {

    /**
     * used to encode the password. This is the same instance that's configured
     * in the Application Context
     */
    @SuppressWarnings("deprecation")
    private final PasswordEncoder passwordEncoder;

    @SuppressWarnings("deprecation")
    public UserStorageImpl(PasswordEncoder passwordEncoder) {
        super(User.class);
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public User create(User entity) {
        // by default, new users get put into the Scientist role. It's up to
        // someone else to elevate them.
        TypedQuery<Role> query = em.createQuery("select r from Role r where r.name=:name", Role.class);
        query.setParameter("name", RoleName.BuiltIn.Scientist.asName());
        Role scientistRole = query.getSingleResult();
        entity.setRole(scientistRole);
        return super.create(entity);
    }

    @Override
    protected void updateFromCaller(User fromDb, User fromUser) {
        String password = fromUser.getPassword();
        if (StringUtils.isNotBlank(password)) {
            if (fromDb.getPasswordStatus() == User.PasswordStatus.assigned) {
                String encodedPassword = passwordEncoder.encodePassword(
                        password, fromDb.getSalt());
                fromDb.setPassword(encodedPassword);
            }
        }
        // todo - if this or others grow in size then we should use Orika or something similar
        fromDb.setFirstName(fromUser.getFirstName());
        fromDb.setLastName(fromUser.getLastName());
        if (fromUser.getRole() != null) {
            fromDb.setRole(fromUser.getRole());
        }
    }

    @Override
    public User findByUUID(String uuid, String email) {
        TypedQuery<User> findByUUID = em.createQuery("select u from User u where u.password = :uuid and u.email=:email", User.class);
        findByUUID.setParameter("uuid", uuid);
        findByUUID.setParameter("email", email);

        return findByUUID.getSingleResult();
    }

    @Override
    public User findByEmail(String email) {
        TypedQuery<User> query = em.createQuery("select u from User u where u.email=:email", User.class);
        query.setParameter("email", email);

        return query.getSingleResult();
    }

    @Override
    public List<User> listAll() {
        TypedQuery<User> findAll = em.createQuery("select u from User u order by u.lastName, u.firstName", User.class);
        return findAll.getResultList();
    }

    @Override
    public List<User> find(String query) {
        TypedQuery<User> q = em.createQuery("select u from User u where " +
                "upper(concat(u.lastName, ' ', u.firstName)) like :query or " +
                "upper(concat(u.firstName, ' ', u.lastName)) like :query or " +
                "upper(u.email) like :query", User.class);
        q.setParameter("query", "%"+query.toUpperCase()+"%");
        return q.getResultList();
    }

    @Override
    @Transactional
    public String resetPassword(Long id) {
        User user = em.find(User.class, id);
        String tmpPassword = UUID.randomUUID().toString();
        user.setPassword(tmpPassword);
        em.merge(user);
        user.setPasswordStatus(User.PasswordStatus.assigned);
        return tmpPassword;
    }

    @Override @Transactional
    public void activate(String uuid, String email, String unsaltedPassword) {
        User user = findByEmail(email);
        if (user.getPassword().equals(uuid)) {
            String encodedPassword = passwordEncoder.encodePassword(
                    unsaltedPassword, user.getSalt());
            user.setPassword(encodedPassword);
        } else {
            throw new IllegalStateException("user account already activated");
        }
        user.setPasswordStatus(User.PasswordStatus.assigned);
        em.merge(user);
    }

    @Override @Transactional
    public void assignRole(Long id, RoleName roleName) {
        User entity = get(id);

        TypedQuery<Role> query = em.createQuery("select r from Role r where r.name=:name", Role.class);
        query.setParameter("name", roleName);
        Role role = query.getSingleResult();
        entity.setRole(role);
        update(id, entity);
    }
}
