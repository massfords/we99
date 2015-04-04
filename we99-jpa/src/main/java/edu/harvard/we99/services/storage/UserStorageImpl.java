package edu.harvard.we99.services.storage;

import com.mysema.query.jpa.impl.JPAQuery;
import edu.harvard.we99.domain.lists.Users;
import edu.harvard.we99.security.RoleName;
import edu.harvard.we99.security.User;
import edu.harvard.we99.services.storage.entities.Mappers;
import edu.harvard.we99.services.storage.entities.QRoleEntity;
import edu.harvard.we99.services.storage.entities.QUserEntity;
import edu.harvard.we99.services.storage.entities.RoleEntity;
import edu.harvard.we99.services.storage.entities.UserEntity;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
        user.setId(null);
        // by default, new users get put into the Scientist role. It's up to
        // someone else to elevate them.
        JPAQuery query = new JPAQuery(em);
        QRoleEntity roles = QRoleEntity.roleEntity;
        query.from(roles).where(roles.name.eq(RoleName.BuiltIn.Scientist.asName()));
        RoleEntity scientistRole = query.uniqueResult(roles);
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

        JPAQuery query = new JPAQuery(em);
        QUserEntity users = QUserEntity.userEntity;
        query.from(users).where(users.password.eq(uuid).and(users.email.eq(email)));

        UserEntity user = query.uniqueResult(users);
        if (user == null) throw new EntityNotFoundException();
        return Mappers.USERS.map(user);
    }

    @Override
    public User findByEmail(String email) {
        UserEntity singleResult = findEntityByEmail(email);
        return Mappers.USERS.map(singleResult);
    }

    @Override
    public Users listAll(Integer page, Integer pageSize) {
        JPAQuery query = new JPAQuery(em);
        QUserEntity users = QUserEntity.userEntity;
        query.from(users).orderBy(users.lastName.asc()).orderBy(users.firstName.asc());
        long count = query.count();
        query.limit(pageSize).offset(pageToFirstResult(page, pageSize));
        List<UserEntity> resultList = query.list(users);
        return map(count, page, pageSize, resultList);
    }

    @Override
    public Users find(String queryText, Integer page, Integer pageSize) {

        String upperQuery = "%" + queryText.toUpperCase() + "%";

        JPAQuery query = new JPAQuery(em);
        QUserEntity users = QUserEntity.userEntity;
        query.from(users)
                .where(
                        users.lastName.concat(", ").concat(users.firstName).toUpperCase().like(upperQuery)
                        .or(users.firstName.concat(" ").concat(users.lastName).toUpperCase().like(upperQuery))
                        .or(users.email.toUpperCase().like(upperQuery))
                )
        ;
        long count = query.count();
        query.limit(pageSize).offset(pageToFirstResult(page, pageSize));

        return map(count, page, pageSize, query.list(users));
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

        JPAQuery query = new JPAQuery(em);
        query.from(QRoleEntity.roleEntity).where(QRoleEntity.roleEntity.name.eq(roleName));

        RoleEntity role = query.uniqueResult(QRoleEntity.roleEntity);
        entity.setRole(role);
        em.merge(entity);
    }

    private Users map(Long count, int page, int pageSize, List<UserEntity> resultList) {
        List<User> list = new ArrayList<>(resultList.size());
        resultList.forEach(ue->list.add(Mappers.USERS.map(ue)));
        return new Users(count, page, pageSize, list);
    }

    private UserEntity findEntityByEmail(String email) {

        JPAQuery query = new JPAQuery(em);
        QUserEntity users = QUserEntity.userEntity;
        query.from(users).where(users.email.eq(email));

        return query.uniqueResult(users);
    }
}
