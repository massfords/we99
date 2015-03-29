package edu.harvard.we99.domain;

import edu.harvard.we99.services.storage.entities.CompoundEntity;
import edu.harvard.we99.services.storage.entities.ExperimentEntity;
import edu.harvard.we99.services.storage.entities.PlateEntity;
import edu.harvard.we99.services.storage.entities.PlateMapEntity;
import edu.harvard.we99.services.storage.entities.PlateTypeEntity;
import edu.harvard.we99.services.storage.entities.ProtocolEntity;
import edu.harvard.we99.services.storage.entities.WellMapEntity;
import org.junit.Assert;
import org.junit.Test;

import javax.persistence.PersistenceException;
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
    public void plateMap() throws Exception {

        beginTx();

        PlateMapEntity pt = new PlateMapEntity()
                .setName(name("Map"))
                .setDescription("My Description")
                .setDim(new PlateDimension(10,16))
                ;

        em.persist(pt);

        commitTx();
    }

    @Test
    public void plateMap_withWells() throws Exception {
        beginTx();

        int ROW_COUNT = 3;
        int COL_COUNT = 4;

        PlateMapEntity pm = new PlateMapEntity()
                .setName(name("Map"))
                .setDescription("My Description")
                .setDim(new PlateDimension(ROW_COUNT, COL_COUNT))
                ;

        pm.withWells(makeWellMaps(ROW_COUNT, COL_COUNT));

        em.persist(pm);

        commitTx();
    }

    @Test
    public void compound() throws Exception {
        beginTx();

        CompoundEntity compound = new CompoundEntity()
                .setName(name("Compound"));

        em.persist(compound);

        commitTx();
    }

    @Test(expected = PersistenceException.class)
    public void compound_dupe() throws Exception {
        beginTx();
        String compoundName = name("C1234");
        CompoundEntity compound = new CompoundEntity().setName(compoundName);
        em.persist(compound);
        commitTx();

        beginTx();
        em.persist(new CompoundEntity().setName(compoundName));
        commitTx();
    }

    @Test
    public void plate() throws Exception {
        beginTx();

        int ROW_COUNT = 3;
        int COL_COUNT = 4;

        ExperimentEntity xp = new ExperimentEntity(name("Exp"));

        ProtocolEntity protocol = new ProtocolEntity().setName(name("P"));
        xp.setProtocol(protocol);

        PlateTypeEntity type = makePlateType(ROW_COUNT, COL_COUNT);

        PlateEntity plate = new PlateEntity()
                .setName(name("Plate"))
                .setDescription("My Description")
                .setPlateType(type)
                .setExperiment(xp)
                .setBarcode("1234");

        plate.withWells(makeWellEntities(ROW_COUNT, COL_COUNT));

        em.persist(protocol);
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
                                .setType(WellType.EMPTY)
                );
            }
        }
        Assert.assertEquals(rowCount * colCount, wells.size());

        return wells.toArray(new WellMapEntity[wells.size()]);
    }
}
