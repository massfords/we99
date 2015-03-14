package edu.harvard.we99.domain;

import edu.harvard.we99.services.storage.entities.CompoundEntity;
import edu.harvard.we99.services.storage.entities.PermissionEntity;
import edu.harvard.we99.services.storage.entities.PlateTypeEntity;
import edu.harvard.we99.services.storage.entities.ProtocolEntity;
import edu.harvard.we99.services.storage.entities.RoleEntity;
import edu.harvard.we99.services.storage.entities.UserEntity;
import org.beanio.BeanReader;
import org.beanio.StreamFactory;
import org.springframework.security.authentication.encoding.PasswordEncoder;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Populates the database with some initial data. We'll only execute this if
 * we just created the database which is a property available on the
 * inspector passed in.
 *
 * @author mford
 */
public class DbPopulator {

    @SuppressWarnings("deprecation")
    private final PasswordEncoder passwordEncoder;

    public DbPopulator(EntityManagerFactory emf,
                       DbVersionInspector inspector,
                       @SuppressWarnings("deprecation") PasswordEncoder encoder) throws Exception {
        this.passwordEncoder = encoder;
        if (!inspector.isDbInitRequired()) {
            // no sample data required, leave now
            return;
        }

        StreamFactory sf = StreamFactory.newInstance();
        sf.loadResource("sample-data/import.xml");
        EntityManager em = emf.createEntityManager();
        try {
            loadCoreData(sf, em);
            Map<String,RoleEntity> roles = loadPermissionData(sf, em);
            loadUsers(sf, em, roles);
        } finally {
            em.close();
        }
    }

    private void loadUsers(StreamFactory sf, EntityManager em, Map<String,RoleEntity> roles) throws IOException {

        List<UserMapping> userMappings =
                loadData(UserMapping.class, sf,
                        "/sample-data/users.csv",
                        "users");

        em.getTransaction().begin();

        for(UserMapping um : userMappings) {
            UserEntity ue = new UserEntity(um.getEmail(), um.getFirstName(), um.getLastName());
            ue.setRole(roles.get(um.getRole()));
            ue.setPasswordStatus(UserEntity.PasswordStatus.assigned);
            em.persist(ue);
            ue.setPassword(encodePassword(um.getPassword(), ue.getSalt()));
            em.merge(ue);
        }

        em.getTransaction().commit();
    }

    private String encodePassword(String password, String salt) {
        return passwordEncoder.encodePassword(password, salt);
    }

    private Map<String,RoleEntity> loadPermissionData(StreamFactory sf, EntityManager em) throws IOException {

        List<PermissionMapping> permissionMappings =
                loadData(PermissionMapping.class, sf,
                        "/sample-data/permissions.csv",
                        "permissions");

        Map<String, RoleEntity> roles = new HashMap<>();

        em.getTransaction().begin();

        for(PermissionMapping pm : permissionMappings) {
            PermissionEntity pe = new PermissionEntity(pm.getPermission());
            em.persist(pe);

            for(String r : pm.getRoles()) {
                RoleEntity re = roles.get(r);
                if (re == null) {
                    re = new RoleEntity(r);
                    roles.put(r, re);
                }
                re.getPermissions().put(pe.getName(), pe);
            }
        }

        roles.values().forEach(em::persist);

        em.getTransaction().commit();
        return roles;
    }

    private void loadCoreData(StreamFactory sf, EntityManager em) throws IOException {

        List<Object> entityList = new ArrayList<>();

        entityList.addAll(loadData(PlateTypeEntity.class, sf, "/sample-data/plate-types.csv", "plateTypes"));
        entityList.addAll(loadData(CompoundEntity.class, sf, "/sample-data/compounds.csv", "compounds"));
        entityList.addAll(loadData(ProtocolEntity.class, sf, "/sample-data/protocols.csv", "protocols"));

        em.getTransaction().begin();
        entityList.forEach(em::persist);
        em.getTransaction().commit();
    }

    private <T> List<T> loadData(Class<T> type, StreamFactory sf, String csvFile, String streamName) throws IOException {
        List<T> list = new ArrayList<>();
        try (Reader r = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(csvFile)))) {
            BeanReader br = sf.createReader(streamName, r);
            Object entity;
            while ((entity = br.read()) != null) {
                list.add(type.cast(entity));
            }
        }
        return list;
    }
}