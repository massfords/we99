package edu.harvard.we99.services.storage;

import edu.harvard.we99.domain.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.TypedQuery;

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
    protected void updateFromCaller(User fromDb, User fromUser) {
        String password = fromUser.getPassword();
        if (StringUtils.isNotBlank(password)) {
            fromDb.setPassword(password);
        }
        // todo - if this or others grow in size then we should use Orika or something similar
        fromDb.setFirstName(fromUser.getFirstName());
        fromDb.setLastName(fromUser.getLastName());
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
        TypedQuery<User> findByUUID = em.createQuery("select u from User u where u.email=:email", User.class);
        findByUUID.setParameter("email", email);

        return findByUUID.getSingleResult();
    }

    @Override @Transactional
    public void activate(String uuid, String email, String unsaltedPassword) {
        User user = findByEmail(email);
        if (user.getPassword().equals(uuid)) {
            user.setPassword(passwordEncoder.encodePassword(unsaltedPassword, user.getSalt()));
        } else {
            throw new IllegalStateException("user account already activated");
        }
    }
}
