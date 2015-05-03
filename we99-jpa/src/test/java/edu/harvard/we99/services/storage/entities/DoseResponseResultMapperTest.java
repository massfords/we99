package edu.harvard.we99.services.storage.entities;

import edu.harvard.we99.domain.Compound;
import edu.harvard.we99.domain.results.DoseResponseResult;
import edu.harvard.we99.test.EastCoastTimezoneRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * @author alan orcharton
 */
public class DoseResponseResultMapperTest {

    @Rule
    public TestRule eastCoastTimezoneRule = new EastCoastTimezoneRule();

    Compound compound = new Compound().setId(200L).setName("Chocolate");

    @Test
    public void domainToEntity_new() throws Exception {
        DoseResponseResult fromCaller = new DoseResponseResult()
                .setCompound(compound)
                .setId(100L)
                .setComments("hello");

        DoseResponseResultEntity dre = Mappers.DOSERESPONSES.mapReverse(fromCaller);
        //id is copied over
        assertEquals(100L,dre.getId().longValue());
        //compound is not copied over
        assertNull(dre.getCompound());
        //description is copied over
        assertEquals("hello",dre.getComments());
    }

    @Test
    public void domainToEntity_existing() throws Exception {

        DoseResponseResult fromCaller = new DoseResponseResult()
                .setComments("hello")
                .setId(100L)
                .setCompound(compound);

        DoseResponseResultEntity dre = new DoseResponseResultEntity();
        Mappers.DOSERESPONSES.mapReverse(fromCaller,dre);
        //id is copied over
        assertEquals(100L,dre.getId().longValue());
        //protocol is not copied over
        assertNull(dre.getCompound());
        //comments is copied over
        assertEquals("hello",dre.getComments());
    }
}
