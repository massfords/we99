package edu.harvard.we99.domain;

import edu.harvard.we99.security.AuthenticatedContext;
import edu.harvard.we99.security.RoleName;
import edu.harvard.we99.security.User;
import edu.harvard.we99.services.CompoundService;
import edu.harvard.we99.services.PlateMapService;
import edu.harvard.we99.services.storage.entities.*;
import org.beanio.BeanReader;
import org.beanio.StreamFactory;
import org.joda.time.DateTime;
import org.springframework.security.authentication.encoding.PasswordEncoder;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.*;

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
                       PlateMapService pms,
                       CompoundService cs,
                       @SuppressWarnings("deprecation") PasswordEncoder encoder) throws Exception {
        this.passwordEncoder = encoder;
        if (!inspector.isDbInitRequired()) {
            // no sample data required, leave now
            return;
        }

        StreamFactory sf = StreamFactory.newInstance();
        sf.loadResource("sample-data/import.xml");
        EntityManager em = emf.createEntityManager();
        try (AuthenticatedContext context = new AuthenticatedContext()) {

            em.merge(new VersionEntity(VersionEntity.Names.DATABASE, DbVersionInspector.hashedDDL()));

            loadCoreData(sf, em);
            Map<String,RoleEntity> roles = loadPermissionData(sf, em);
            loadUsers(sf, em, roles);

            UserEntity ue = selectAdmin(em);
            User admin = Mappers.USERS.map(ue);
            context.install(admin);

            loadCompounds(cs);


            loadExperiments(sf, em);

            // add plates
            pms.create("5x5", getClass().getResourceAsStream("/sample-data/platemap5x5.csv"));
        } finally {
            em.close();
        }
    }

    private void loadCompounds(CompoundService cs) {
        cs.upload(getClass().getResourceAsStream("/sample-data/compounds.csv"));
    }


    private List<WellAmountMapping> loadWellContents(StreamFactory sf) throws IOException {
        // create new doses
        List<WellAmountMapping> amounts =  loadData(WellAmountMapping.class, sf,
                "/sample-data/wellamounts.csv","wellamounts");
        return amounts;

    }




    private void loadExperiments(StreamFactory sf, EntityManager em) throws IOException {

        Random rand = new Random();
        // get the corning plate type
        List<ExperimentMapping> experimentMappings = loadData(
                ExperimentMapping.class, sf,
                "/sample-data/experiments.csv", "experiments");

        em.getTransaction().begin();

        TypedQuery<UserEntity> query = em.createQuery("select u from UserEntity u where u.role.name = :rolename", UserEntity.class);
        query.setParameter("rolename", RoleName.BuiltIn.Admin.asName());
        List<UserEntity> userEntities = query.getResultList();

        PlateTypeEntity pte = em.createQuery("select pte from PlateTypeEntity pte", PlateTypeEntity.class).getResultList().get(0);
        CompoundEntity cmpe = em.createQuery("select cmpe from CompoundEntity cmpe", CompoundEntity.class).getResultList().get(0);

        // Used for assigning random compounds.
        List <CompoundEntity> compounds = em.createQuery("select cmpe from CompoundEntity cmpe", CompoundEntity.class).getResultList().subList(0,20);

        Compound comp1 = new Compound(cmpe.getId(),cmpe.getName());

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

                PlateResultEntity pre = new PlateResultEntity();
                pre.setSource("Test Lab");
                pre.setPlate(pe);
                pe.setResults(pre);

                em.persist(pre);
                em.persist(pe);
                pte.addPlate(pe);
                em.merge(pte);

                // Used for the well result assignment date.
                DateTime analysisDate = new DateTime().minusMinutes(rand.nextInt(10000));
                for(int row=0; row<pte.getDim().getRows(); row++) {
                    for(int col=0; col<pte.getDim().getCols(); col++) {
                        WellEntity we = new WellEntity(row, col);
                        we.setLabel("loc", "well" + row + "," + col);

                        // Add control wells if you're in the first or last column.
                        // Positive are on the top of the plate, and negative are on
                        // the bottom of the plate.
                        if(col == 0 || col == pte.getDim().getCols() - 1){
                            if(row / pte.getDim().getRows() > 0.5) {
                                we.setType(WellType.NEGATIVE);
                            }else{
                                we.setType(WellType.POSITIVE);
                            }
                        }else{
                            we.setType(WellType.COMP);
                        }
                        Set <DoseEntity> doses = new HashSet<>();

                        Amount amount = new Amount();
                        amount.setUnits(DoseUnit.MICROMOLAR);
                        amount.setNumber(Math.random());

                        DoseEntity dose = new DoseEntity();
                        dose.setCompound(compounds.get(rand.nextInt(20)));
                        dose.setWell(we);
                        dose.setAmount(amount);
                        doses.add(dose);
                        we.setContents(doses);

                        pe.withWells(we);
                        em.persist(we);
                        em.merge(pe);

                        // Create a random well result.
                        WellResultsEntity wre = new WellResultsEntity(row, col);
                        wre.addSample(new SampleEntity().setMeasuredAt(analysisDate).setValue(rand.nextDouble()));
                        pre.add(wre);
                        em.persist(wre);
                        em.merge(pre);

                    }
                }

            }
            PlateEntity peDose = new PlateEntity()
                    .setName("plate dose resp for exp " + ee.getId())
                    .setBarcode("doseresp1")
                    .setExperiment(ee)
                    .setPlateType(pte);
            em.persist(peDose);
            pte.addPlate(peDose);
            em.merge(pte);
            List<WellAmountMapping> wellcontents = loadWellContents(sf);
            wellcontents.forEach(wcontent -> {
                WellEntity we = new WellEntity(wcontent.getRow(), wcontent.getCol());
                we.setLabel("loc", "well" + wcontent.getRow() + "," + wcontent.getCol());
                we.setType(wcontent.getType());
                DoseEntity de = new DoseEntity().setCompound(cmpe);
                Set<DoseEntity> doseSet = new HashSet<>();
                doseSet.add(de.setAmount(new Amount(wcontent.getNumber(), wcontent.getUnits())));
                we.setContents(doseSet);
                peDose.withWells(we);
                em.persist(we);
                em.merge(peDose);
            });

            
            
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

    private UserEntity selectAdmin(EntityManager em) throws Exception {
        TypedQuery<UserEntity> query = em.createQuery("select ue from UserEntity ue where ue.role.name=:roleName", UserEntity.class);
        query.setParameter("roleName", RoleName.BuiltIn.Admin.asName());
        List<UserEntity> users = query.getResultList();
        return users.get(0);
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
