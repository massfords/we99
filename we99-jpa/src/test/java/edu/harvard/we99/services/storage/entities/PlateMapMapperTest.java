package edu.harvard.we99.services.storage.entities;

import edu.harvard.we99.domain.Coordinate;
import edu.harvard.we99.domain.PlateDimension;
import edu.harvard.we99.domain.PlateMap;
import edu.harvard.we99.domain.WellMap;
import edu.harvard.we99.domain.WellType;
import org.junit.Test;

import java.util.Map;

import static edu.harvard.we99.test.BaseFixture.assertJsonEquals;
import static edu.harvard.we99.test.BaseFixture.load;
import static edu.harvard.we99.util.JacksonUtil.toJsonString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

/**
 * @author mford
 */
public class PlateMapMapperTest {

    @Test
    public void domainToEntity_new() throws Exception {
        PlateMap pm = makePlateMap();

        PlateMapEntity pme = Mappers.PLATEMAP.mapReverse(pm);
        // id is copied over
        assertEquals(pm.getId(), pme.getId());
        assertEquals("plateMap", pme.getName());
        assertEquals("desc", pme.getDescription());
        // wells are copied over manually
        assertEquals(0, pme.getWells().size());
    }

    @Test
    public void domainToEntity_existing() throws Exception {
        PlateMap pm = makePlateMap();

        PlateMapEntity pme = makePlateEntity();
        Map<Coordinate, WellMapEntity> wells = pme.getWells();

        Mappers.PLATEMAP.mapReverse(pm, pme);
        // id is mapped
        assertEquals(pm.getId(), pme.getId());
        // name/desc are mapped
        assertEquals(pm.getName(), pme.getName());
        assertEquals(pm.getDescription(), pme.getDescription());

        // we're copying the wells manually so the map shouldn't have changed
        assertSame(wells, pme.getWells());
    }

    @Test
    public void entityToDomain() throws Exception {
        PlateMapEntity pme = makePlateEntity();
        PlateMap pm = Mappers.PLATEMAP.map(pme);
        assertJsonEquals(load("/Mappers/plateMap.json"), toJsonString(pm));
    }

    private PlateMapEntity makePlateEntity() {
        PlateMapEntity pme = new PlateMapEntity()
                .setId(300L)
                .setDescription("foo123")
                .setName("pme").setDim(new PlateDimension(5,6));
        Coordinate coord = new Coordinate(4, 5);
        pme.getWells().put(
                coord,
                new WellMapEntity(coord)
                        .setId(800L)
                        .setLabel("lbl", "ABC")
                        .setType(WellType.COMP)
        );
        return pme;
    }

    private WellMap makeWell() {
        return new WellMap(1, 2)
                .setId(1234L)
                .setType(WellType.COMP).withLabel("lbl", "A");
    }

    private PlateMap makePlateMap() {
        return new PlateMap()
                .setName("plateMap")
                .setDescription("desc")
                .setId(100L)
                .withWells(makeWell());
    }
}
