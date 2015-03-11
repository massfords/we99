package edu.harvard.we99.services.storage.entities;

import edu.harvard.we99.domain.Compound;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * @author mford
 */
public class CompoundMapperTest {

    @Test
    public void domainToEntity_existing() throws Exception {
        Compound compound = new Compound(100L, "c1");
        CompoundEntity ce = Mappers.COMPOUND.mapReverse(compound);
        assertEquals(100L, ce.getId().longValue());
        assertEquals("c1", ce.getName());
    }

    @Test
    public void domainToEntity_new() throws Exception {
        Compound compound = new Compound("c1");
        CompoundEntity ce = Mappers.COMPOUND.mapReverse(compound);
        assertNull(ce.getId());
        assertEquals("c1", ce.getName());
    }

    @Test
    public void entityToDomain() throws Exception {
        CompoundEntity ce = new CompoundEntity(100L, "c1");
        Compound c = Mappers.COMPOUND.map(ce);
        assertEquals(100L, c.getId().longValue());
        assertEquals("c1", c.getName());
    }
}
