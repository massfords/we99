package edu.harvard.we99.domain;

import edu.harvard.we99.services.storage.entities.CompoundEntity;
import edu.harvard.we99.services.storage.entities.ExperimentEntity;
import edu.harvard.we99.services.storage.entities.PlateEntity;
import edu.harvard.we99.services.storage.entities.PlateMapEntity;
import edu.harvard.we99.services.storage.entities.PlateTypeEntity;
import edu.harvard.we99.services.storage.entities.WellMapEntity;
import org.junit.Assert;
import org.junit.Test;

import javax.persistence.PersistenceException;
import javax.validation.ConstraintViolationException;
import java.util.HashSet;
import java.util.Set;

import static edu.harvard.we99.test.BaseFixture.name;

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
public class JpaIT extends JpaSpringFixture {

    @Test
    public void plateTemplate() throws Exception {

        beginTx();

        PlateTypeEntity type = makePlateType(10, 16);

        PlateMapEntity pt = new PlateMapEntity()
                .withName(name("Map"))
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

        PlateMapEntity pt = new PlateMapEntity()
                .withName(name("Map"))
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

        PlateTypeEntity type = makePlateType(ROW_COUNT, COL_COUNT);

        PlateMapEntity pm = new PlateMapEntity()
                .withName(name("Map"))
                .withDescription("My Description")
                .withPlateType(type)
                ;

        pm.withWells(makeWellMaps(ROW_COUNT, COL_COUNT));

        em.persist(type);
        em.persist(pm);

        commitTx();
    }

    @Test(expected = PersistenceException.class)
    public void plateTemplate_wellOutOfBounds() throws Exception {
        beginTx();

        int ROW_COUNT = 3;
        int COL_COUNT = 4;

        PlateTypeEntity type = makePlateType(ROW_COUNT, COL_COUNT);

        PlateMapEntity pt = new PlateMapEntity()
                .withName(name("Map"))
                .withDescription("My Description")
                .withPlateType(type)
                .withWells(new WellMapEntity(100, 200)
                        .withType(WellType.EMPTY))
                ;

        em.persist(type);
        em.persist(pt);

        commitTx();
    }

    @Test
    public void compound() throws Exception {
        beginTx();

        CompoundEntity compound = new CompoundEntity()
                .withName(name("Compound"));

        em.persist(compound);

        commitTx();
    }

    @Test(expected = PersistenceException.class)
    public void compound_dupe() throws Exception {
        beginTx();
        String compoundName = name("C1234");
        CompoundEntity compound = new CompoundEntity().withName(compoundName);
        em.persist(compound);
        commitTx();

        beginTx();
        em.persist(new CompoundEntity().withName(compoundName));
        commitTx();
    }

    @Test
    public void plate() throws Exception {
        beginTx();

        int ROW_COUNT = 3;
        int COL_COUNT = 4;

        ExperimentEntity xp = new ExperimentEntity(name("Exp"));

        PlateTypeEntity type = makePlateType(ROW_COUNT, COL_COUNT);

        PlateEntity plate = new PlateEntity()
                .withName(name("Plate"))
                .withDescription("My Description")
                .withPlateType(type)
                .withExperiment(xp)
                .withBarcode("1234");

        plate.withWells(makeWellEntities(ROW_COUNT, COL_COUNT));

        em.persist(xp);
        em.persist(type);
        em.persist(plate);

        commitTx();
    }



    private WellMapEntity[] makeWellMaps(int rowCount, int colCount) {
        Set<WellMapEntity> wells = new HashSet<>();

        for(int row=0; row< rowCount; row++) {
            for(int col=0; col< colCount; col++) {
                wells.add(
                        new WellMapEntity(row, col)
                                .withType(WellType.EMPTY)
                );
            }
        }
        Assert.assertEquals(rowCount * colCount, wells.size());

        return wells.toArray(new WellMapEntity[wells.size()]);
    }
}
