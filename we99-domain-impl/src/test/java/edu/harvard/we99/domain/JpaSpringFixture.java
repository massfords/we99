package edu.harvard.we99.domain;

import edu.harvard.we99.services.storage.UserStorage;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.HashSet;
import java.util.Set;

import static edu.harvard.we99.test.BaseFixture.name;

/**
 * @author mford
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"/services-context.xml", "/test-context.xml", "/application-context.xml"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public abstract class JpaSpringFixture {
    @Inject
    private EntityManagerFactory emf;
    @Inject
    private UserStorage userStorage;
    protected EntityManager em;

    @Before
    public void setUp() throws Exception {
        this.em = emf.createEntityManager();
    }

    @After
    public void tearDown() throws Exception {
        this.em.close();
    }

    protected void commitTx() {
        em.getTransaction().commit();
    }

    protected void beginTx() {
        em.getTransaction().begin();
    }

    public UserStorage getUserStorage() {
        return userStorage;
    }

    protected static Well[] makeWells(int rowCount, int colCount) {
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

    protected static PlateType makePlateType(int ROW_COUNT, int COL_COUNT) {
        return new PlateType()
                .withDim(new PlateDimension(ROW_COUNT, COL_COUNT))
                .withName(name("plateType"))
                .withManufacturer("Foo Company");
    }
}
