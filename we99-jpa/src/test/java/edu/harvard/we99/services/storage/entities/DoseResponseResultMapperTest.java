package edu.harvard.we99.services.storage.entities;

import edu.harvard.we99.domain.*;
import edu.harvard.we99.domain.results.DoseResponseResult;
import edu.harvard.we99.services.storage.DoseResponseResultStorage;
import edu.harvard.we99.services.storage.DoseResponseResultStorageImpl;
import edu.harvard.we99.test.EastCoastTimezoneRule;
import org.joda.time.DateTime;
import org.junit.Rule;
import org.junit.Test;

import javax.inject.Inject;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static edu.harvard.we99.test.BaseFixture.assertJsonEquals;
import static edu.harvard.we99.test.BaseFixture.load;
import static edu.harvard.we99.util.JacksonUtil.toJsonString;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * @author alan orcharton
 */
public class DoseResponseResultMapperTest {


    @Rule
    public EastCoastTimezoneRule eastCoastTimezoneRule = new EastCoastTimezoneRule();

    Protocol protocol = new Protocol().withId(200L).withName("proto1");
    ProtocolEntity protocolEntity = new ProtocolEntity().setId(200L).setName("proto1");

    Compound compound = new Compound().setId(200L).setName("Chocolate");
    CompoundEntity compoundEntity = new CompoundEntity().setId(200L).setName("Chocolate");

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


    @Test
         public void entityToDomain() throws Exception {
        DoseResponseResultEntity dre = new DoseResponseResultEntity()
                .setId(300L)
                .setCompound(compoundEntity)
                .setCreated(new DateTime("2015-01-02T10:30:20Z"))
                .setComments("dose response");

       // dre.getDoseWells().put()
       // Map<Long,WellEntity> wm = new HashMap<>();
        WellEntity w = makeWell();

       // wm.put(100L,w);
        //dre.setDoseWells(wm);

        dre.getDoseWells().put(100L, w);

        DoseResponseResult fromCaller = new DoseResponseResult()
                .setCompound(compound)
                .setId(100L)
                .setComments("no map");

        fromCaller.getDoseWells().put(101L,new Well(1,1).setId(23L));
        DoseResponseResult drr = Mappers.DOSERESPONSES.map(dre);
        //assertJsonEquals(load("/Mappers/doseresponseresult.json"), toJsonString(fromCaller));
    }
    private WellEntity makeWell() {
        return new WellEntity(1,2).setId(15L);
    }


}
