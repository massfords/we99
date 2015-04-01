package edu.harvard.we99.domain;

import edu.harvard.we99.security.RoleName;
import edu.harvard.we99.services.PlateMapService;
import edu.harvard.we99.services.storage.entities.CompoundEntity;
import edu.harvard.we99.services.storage.entities.ExperimentEntity;
import edu.harvard.we99.services.storage.entities.PermissionEntity;
import edu.harvard.we99.services.storage.entities.PlateEntity;
import edu.harvard.we99.services.storage.entities.PlateTypeEntity;
import edu.harvard.we99.services.storage.entities.ProtocolEntity;
import edu.harvard.we99.services.storage.entities.RoleEntity;
import edu.harvard.we99.services.storage.entities.UserEntity;
import edu.harvard.we99.services.storage.entities.WellEntity;
import org.beanio.BeanReader;
import org.beanio.StreamFactory;
import org.springframework.security.authentication.encoding.PasswordEncoder;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.io.IOException;
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
    private final PlateMapService plateMapService;

    public DbPopulator(EntityManagerFactory emf,
                       DbVersionInspector inspector,
                       PlateMapService pms,
                       @SuppressWarnings("deprecation") PasswordEncoder encoder) throws Exception {
        this.passwordEncoder = encoder;
        this.plateMapService = pms;
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
            loadExperiments(sf, em);

            // add plates
            plateMapService.createUnsecured("5x5", getClass().getResourceAsStream("/sample-data/platemap5x5.csv"));
        } finally {
            em.close();
        }
    }

    private void loadExperiments(StreamFactory sf, EntityManager em) throws IOException {
        // get the corning plate type
        List<ExperimentMapping> experimentMappings = loadData(
                ExperimentMapping.class, sf,
                "/sample-data/experiments.csv", "experiments");

        em.getTransaction().begin();

        TypedQuery<UserEntity> query = em.createQuery("select u from UserEntity u where u.role.name = :rolename", UserEntity.class);
        query.setParameter("rolename", RoleName.BuiltIn.Admin.asName());
        List<UserEntity> userEntities = query.getResultList();

        PlateTypeEntity pte = em.createQuery("select pte from PlateTypeEntity pte", PlateTypeEntity.class).getResultList().get(0);



        for(ExperimentMapping expMapping : experimentMappings) {
            ExperimentEntity ee = new ExperimentEntity(expMapping.getName())
                    .setDescription(expMapping.getDesc())
                    .setStatus(expMapping.getStatus())
                    .setProtocol(selectProtocol(em, expMapping));
            userEntities.forEach(ee::addUser);
            em.persist(ee);
            userEntities.forEach(ue -> ue.addExperiment(ee));
            userEntities.forEach(em::merge);

            // add some plates to the experiment
            for(int i=0; i<3; i++) {
                PlateEntity pe = new PlateEntity()
                        .setName("plate " + i + " for exp " + ee.getId())
                        .setBarcode("abc" + i)
                        .setExperiment(ee)
                        .setPlateType(pte);
                em.persist(pe);
                pte.addPlate(pe);
                em.merge(pte);
                for(int row=0; row<pte.getDim().getRows(); row++) {
                    for(int col=0; col<pte.getDim().getCols(); col++) {
                        WellEntity we = new WellEntity(row, col);
                        we.setLabel("loc", "well" + row + "," + col);
                        we.setType(WellType.EMPTY);
                        pe.withWells(we);
                        em.persist(we);
                        em.merge(pe);
                    }
                }
            }
        }

        em.getTransaction().commit();
    }

    private ProtocolEntity selectProtocol(EntityManager em, ExperimentMapping expMapping) {
        ProtocolEntity pe;
        try {
            TypedQuery<ProtocolEntity> query = em.createQuery(
                    "select pe from ProtocolEntity as pe where pe.name=:name", ProtocolEntity.class);
            query.setParameter("name", expMapping.getProtocol());
            pe = query.getSingleResult();
        } catch(NoResultException e) {
            pe = new ProtocolEntity(expMapping.getProtocol());
            em.persist(pe);
        }
        return pe;
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
