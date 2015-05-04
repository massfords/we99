package edu.harvard.we99.domain;

import edu.harvard.we99.domain.results.PlateMetrics;
import edu.harvard.we99.domain.results.analysis.PlateMetricsFunction;
import edu.harvard.we99.security.AuthenticatedContext;
import edu.harvard.we99.security.RoleName;
import edu.harvard.we99.security.User;
import edu.harvard.we99.services.CompoundService;
import edu.harvard.we99.services.PlateMapService;
import edu.harvard.we99.services.storage.entities.CompoundEntity;
import edu.harvard.we99.services.storage.entities.DoseEntity;
import edu.harvard.we99.services.storage.entities.ExperimentEntity;
import edu.harvard.we99.services.storage.entities.Mappers;
import edu.harvard.we99.services.storage.entities.PermissionEntity;
import edu.harvard.we99.services.storage.entities.PlateEntity;
import edu.harvard.we99.services.storage.entities.PlateMetricsEntity;
import edu.harvard.we99.services.storage.entities.PlateResultEntity;
import edu.harvard.we99.services.storage.entities.PlateTypeEntity;
import edu.harvard.we99.services.storage.entities.ProtocolEntity;
import edu.harvard.we99.services.storage.entities.RoleEntity;
import edu.harvard.we99.services.storage.entities.SampleEntity;
import edu.harvard.we99.services.storage.entities.UserEntity;
import edu.harvard.we99.services.storage.entities.VersionEntity;
import edu.harvard.we99.services.storage.entities.WellEntity;
import edu.harvard.we99.services.storage.entities.WellResultsEntity;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

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
            loadDoseResponseExperiment(sf, em);

            // add plates
            pms.create("24x16", getClass().getResourceAsStream("/sample-data/platemap24x16.csv"));
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


    private List<WellResultsEntity> loadDrPlateResults(StreamFactory sf) throws IOException {

       List<WellResultsEntity> wr = loadData(WellResultsEntity.class, sf, "/sample-data/drplateresults.csv","results");

       return wr;
    }

    private void loadDoseResponseExperiment(StreamFactory sf, EntityManager em) throws IOException{


        ExperimentMapping emap = new ExperimentMapping();
        emap.setName("experiment dose");
        emap.setDesc("Experiment for Dose Response");
        emap.setProtocol("Alpha");
        emap.setStatus(ExperimentStatus.UNPUBLISHED);

        em.getTransaction().begin();

        TypedQuery<UserEntity> query = em.createQuery("select u from UserEntity u where u.role.name = :rolename", UserEntity.class);
        query.setParameter("rolename", RoleName.BuiltIn.Admin.asName());
        List<UserEntity> userEntities = query.getResultList();

        ExperimentEntity ee = new ExperimentEntity(emap.getName())
                .setDescription(emap.getDesc())
                .setStatus(emap.getStatus())
                .setProtocol(selectProtocol(em, emap));

        userEntities.forEach(ee::addUser);
        em.persist(ee);
        userEntities.forEach(ue -> ue.addExperiment(ee));
        userEntities.forEach(em::merge);


        PlateTypeEntity pte = new PlateTypeEntity()
                .setName("DoseResponsePlate")
                .setDim(new PlateDimension(16, 24))
                .setManufacturer("Man");

        em.persist(pte);

        //add a dose response plate and data from file.
        PlateEntity peDose = new PlateEntity()
                .setName("plate dose resp for exp " + ee.getId())
                .setBarcode("doseresp1")
                .setExperiment(ee)
                .setPlateType(pte);

        em.persist(peDose);
        pte.addPlate(peDose);
        em.merge(pte);

        CompoundEntity cmpe = em.createQuery("select cmpe from CompoundEntity cmpe where cmpe.name = 'Urea'",
                CompoundEntity.class).getResultList().get(0);

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
        List<WellResultsEntity> wresults = loadDrPlateResults(sf);
        PlateResultEntity pre = new PlateResultEntity().setPlate(peDose).setSource("testdata");
        wresults.forEach(r -> {
            pre.add(r);
            em.persist(r);
        });
        peDose.setResults(pre);
        em.persist(pre);
        em.merge(peDose);

        em.getTransaction().commit();



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



        // Used for assigning random compounds.
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
            for(int i=0; i<25; i++) {

                PlateEntity pe = new PlateEntity()
                        .setName("plate " + i + " for exp " + ee.getId())
                        .setBarcode("abc" + i)
                        .setExperiment(ee)
                        .setPlateType(pte);

                PlateResultEntity pre = new PlateResultEntity();
                pre.setSource("Test Lab");
                pre.setPlate(pe);
                pe.setResults(pre);
                pte.addPlate(pe);

                em.persist(pre);
                em.persist(pe);

                // Used for the well result assignment date.
                CompoundEntity currentCompound = null;
                Set <String> compounds = new HashSet<>();

                DateTime analysisDate = new DateTime().minusMinutes(rand.nextInt(10000));
                for(int row=0; row<pte.getDim().getRows(); row++) {

                    Double variance = (Math.random());

                    // Make a new compound at the start of a row.
                    String compoundName = "C" + rand.nextInt(2000) + "-" + rand.nextInt(2000);
                    while(compounds.contains(compoundName)){
                        compoundName = "C" + rand.nextInt(2000) + "-" + rand.nextInt(2000);
                    }
                    currentCompound = new CompoundEntity().setName(compoundName);
                    em.persist(currentCompound);

                    for(int col=0; col<pte.getDim().getCols(); col++) {

                        WellEntity we = new WellEntity(row, col);
                        we.setLabel("loc", "well" + row + "x" + col);

                        // Add control wells if you're in the first or last column.
                        // Positive are on the top of the plate, and negative are on
                        // the bottom of the plate.
                        Double compAmount = ( col ) * (200.0 / (pte.getDim().getCols() ));
                        Double value =  ( 1 /(1 + Math.exp( -0.07 * (compAmount - 100.0))) ) + (variance * (Math.random() - 1));
                        if(col == 0 || col == pte.getDim().getCols() - 1){
                            if(row  <  pte.getDim().getRows() / 2) {
                                we.setType(WellType.NEGATIVE);
                                value = 0.0 + (Math.random() / 10.0);
                            }else{
                                we.setType(WellType.POSITIVE);
                                value = 1.0 - (Math.random() / 10.0);
                            }
                        }else{
                            we.setType(WellType.COMP);
                        }
                        Set <DoseEntity> doses = new HashSet<>();

                        Amount amount = new Amount();
                        amount.setUnits(DoseUnit.MICROMOLAR);
                        amount.setNumber(compAmount);

                        DoseEntity dose = new DoseEntity();
                        dose.setCompound(currentCompound);
                        dose.setWell(we);
                        dose.setAmount(amount);
                        doses.add(dose);
                        we.setContents(doses);

                        pe.withWells(we);
                        em.persist(we);

                        // Create a random well result.
                        WellResultsEntity wre = new WellResultsEntity(row, col);
                        wre.addSample(new SampleEntity()
                                .setMeasuredAt(analysisDate)
                                .setValue(value)
                                .setLabel("")
                        );

                        /* Low rent csv file generator
                        System.out.println(
                            row + "," +
                            col + "," +
                            we.getType().toString() + "," +
                            we.getLabels().iterator().next().getName() + "," +
                            we.getLabels().iterator().next().getValue() + "," +
                            we.getContents().iterator().next().getCompound().getName() + ","
                            + we.getContents().iterator().next().getAmount().getNumber().toString() + ","
                            + we.getContents().iterator().next().getAmount().getUnits().toString() + ","
                            + wre.getSamples().iterator().next().getValue()
                         );
                         */
                        pre.add(wre);
                        em.persist(wre);

                    }
                }

                PlateMetricsFunction pmf = new PlateMetricsFunction(Mappers.PLATES.map(pe));
                PlateMetrics pmetrics = pmf.apply(new LinkedList<>(Mappers.PLATERESULT.map(pre).getWellResults().values())).get(0);

                PlateMetricsEntity pme = new PlateMetricsEntity();
                pme.setAvgNegative(pmetrics.getAvgNegative());
                pme.setAvgPositive(pmetrics.getAvgPositive());
                pme.setLabel(pmetrics.getLabel());
                pme.setZee(pmetrics.getZee());
                pme.setZeePrime(pmetrics.getZeePrime());
                em.persist(pme);
//                pre.getMetrics().put("BASIC_PLATE_SUMMARY", pme);
                pre.addMetrics(pme);


//                Map <String, DoseResponseResultEntity> drMap = new HashMap <>();
//                for(WellResultsEntity wr : pre.getWellResults().values()){
//                    for(WellEntity we : pe.getWells().values()){
//                        if(we.getCoordinate().equals(wr.getCoordinate())){
//                            CompoundEntity c = we.getContents().iterator().next().getCompound();
//                            if(!drMap.containsKey(c.getName())){
//                                drMap.put(c.getName(), new DoseResponseResultEntity());
//                            }
//                            DoseResponseResultEntity drr = drMap.get(c.getName());
//                            drr.setCompound(c);
//                            drr.setDoses(new LinkedList<>(we.getContents()));
//                            drr.setExperiment(ee);
//                            drr.getExperimentPoints().add(new ExperimentPoint()
//                                .setDoseId(we.getContents().iterator().next().getId())
//                                .setPlateId(pe.getId())
//                                .setX(we.getContents().iterator().next().getAmount().getNumber())
//                                .setY(wr.getSamples().iterator().next().getValue())
//                            );
//                        }
//                    }
//                }

//                for(DoseResponseResultEntity drre : drMap.values()){
//
//                    em.persist(drre);
//                    DoseResponseResult doseResponseResult = Mappers.DOSERESPONSES.map(drre);
//                    doseResponseResult = CurveFit.fitCurve(doseResponseResult);
//                    DoseResponseResultEntity processed = Mappers.DOSERESPONSES.mapReverse(doseResponseResult);
//                    if(processed != null){
//                        em.persist(processed);
//                    }
//
//                }



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
