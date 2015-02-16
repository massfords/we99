package edu.harvard.we99.domain;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceException;
import javax.validation.ConstraintViolationException;
import java.util.HashSet;
import java.util.Set;

/**
 * Tests the reading and writing of entities to the database. This will
 * exercise the JPA annotations to ensure that the relationships are modeled
 * as we expect and that there aren't any errors in the annotations.
 *
 * I would expect most of the tests for our real behavior to exist in the
 * web tier since those will add the additional aspect of testing the the
 * services that wrap the persistence layer as well as the transport layer.
 *
 * @author mford
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"/test-context.xml","/application-context.xml"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class JpaIT {

    @Inject
    private EntityManagerFactory emf;
    private EntityManager em;

    @Before
    public void setUp() throws Exception {
        this.em = emf.createEntityManager();
    }

    @After
    public void tearDown() throws Exception {
        this.em.close();
    }

    @Test
    public void plateTemplate() throws Exception {

        beginTx();

        PlateType type = new PlateType()
                .withRows(10)
                .withCols(16)
                .withManufacturer("Foo Company");

        PlateTemplate pt = new PlateTemplate()
                .withName("Template 1")
                .withDescription("My Description")
                .withPlateType(type)
                ;

        em.persist(type);
        em.persist(pt);

        commitTx();
    }

    @Test(expected = ConstraintViolationException.class)
    public void plateTemplate_missingPlateType() throws Exception {

        beginTx();

        PlateTemplate pt = new PlateTemplate()
                .withName("Template 1")
                .withDescription("My Description")
                ;

        em.persist(pt);
        commitTx();
    }

    @Test
    public void plateTemplate_withWells() throws Exception {
        beginTx();

        int ROW_COUNT = 3;
        int COL_COUNT = 4;

        PlateType type = new PlateType()
                .withRows(ROW_COUNT)
                .withCols(COL_COUNT)
                .withManufacturer("Foo Company");

        PlateTemplate pt = new PlateTemplate()
                .withName("Template 1")
                .withDescription("My Description")
                .withPlateType(type)
                ;

        pt.withWells(makeWells(ROW_COUNT, COL_COUNT));

        em.persist(type);
        em.persist(pt);

        commitTx();
    }

    @Test(expected = PersistenceException.class)
    public void plateTemplate_wellOutOfBounds() throws Exception {
        beginTx();

        int ROW_COUNT = 3;
        int COL_COUNT = 4;

        PlateType type = new PlateType()
                .withRows(ROW_COUNT)
                .withCols(COL_COUNT)
                .withManufacturer("Foo Company");

        PlateTemplate pt = new PlateTemplate()
                .withName("Template 1")
                .withDescription("My Description")
                .withPlateType(type)
                .withWells(new Well(100, 200)
                        .withType(WellType.EMPTY))
                ;

        em.persist(type);
        em.persist(pt);

        commitTx();
    }

    @Test
    public void plateTemplate_withWells_withDoses() throws Exception {
        beginTx();

        Compound soda = new Compound().withName("H20");
        Compound scotch = new Compound().withName("Single Malt");

        int ROW_COUNT = 3;
        int COL_COUNT = 4;

        PlateType type = new PlateType()
                .withRows(ROW_COUNT)
                .withCols(COL_COUNT)
                .withManufacturer("Foo Company");

        PlateTemplate pt = new PlateTemplate()
                .withName("Template 1")
                .withDescription("My Description")
                .withPlateType(type);

        pt.withWells(makeWells(ROW_COUNT, COL_COUNT));

        int counter = 1;
        for(Well well : pt.getWells().values()) {
            Dose dose = new Dose().withWell(well)
                    .withNumber(50)
                    .withUnits(DoseUnit.MILLIS);
            if (counter % 2 == 0) {
                dose.withCompound(scotch);
            } else {
                dose.withCompound(soda);
            }
            well.dose(dose);
            counter++;
        }

        em.persist(soda);
        em.persist(scotch);
        em.persist(type);
        em.persist(pt);

        commitTx();
    }

    @Test
    public void compound() throws Exception {
        beginTx();

        Compound compound = new Compound().withName("C1234");

        em.persist(compound);

        commitTx();
    }

    @Test(expected = PersistenceException.class)
    public void compound_dupe() throws Exception {
        beginTx();
        String compoundName = "C1234";
        Compound compound = new Compound().withName(compoundName);
        em.persist(compound);
        commitTx();

        beginTx();
        em.persist(new Compound().withName(compoundName));
        commitTx();
    }

    @Test
    public void plate() throws Exception {
        beginTx();

        int ROW_COUNT = 3;
        int COL_COUNT = 4;

        PlateType type = new PlateType()
                .withRows(ROW_COUNT)
                .withCols(COL_COUNT)
                .withManufacturer("Foo Company");

        Plate plate = new Plate()
                .withName("Plate 1")
                .withDescription("My Description")
                .withPlateType(type)
                .withBarcode(1234L);

        plate.withWells(makeWells(ROW_COUNT, COL_COUNT));

        em.persist(type);
        em.persist(plate);

        commitTx();

    }


    private void commitTx() {
        em.getTransaction().commit();
    }

    private void beginTx() {
        em.getTransaction().begin();
    }

    private Well[] makeWells(int rowCount, int colCount) {
        Set<Well> wells = new HashSet<>();

        for(int row=0; row< rowCount; row++) {
            for(int col=0; col< colCount; col++) {
                wells.add(
                        new Well(row, col)
                                .withType(WellType.EMPTY)
                );
            }
        }
        Assert.assertEquals(rowCount * colCount, wells.size());

        return wells.toArray(new Well[wells.size()]);
    }
}
