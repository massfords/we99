package edu.harvard.we99.services.storage.entities;

import edu.harvard.we99.domain.Coordinate;
import edu.harvard.we99.domain.PlateDimension;
import edu.harvard.we99.domain.PlateMap;
import edu.harvard.we99.domain.PlateType;
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
    private final PlateType plateType = new PlateType().withId(250L)
            .withDim(new PlateDimension(10, 20))
            .withName("foo").withManufacturer("Man1");

    private final PlateTypeEntity plateTypeEntity =
            new PlateTypeEntity().withId(250L)
                    .withDim(new PlateDimension(10, 20))
                    .withName("foo").withManufacturer("Man1");

    @Test
    public void domainToEntity_new() throws Exception {
        PlateMap pm = makePlateMap();

        PlateMapEntity pme = Mappers.PLATEMAP.mapReverse(pm);
        // id is copied over
        assertEquals(pm.getId(), pme.getId());
        assertEquals("plateMap", pme.getName());
        assertEquals("desc", pme.getDescription());
        assertEquals(250L, pme.getPlateType().getId().longValue());
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
        assertEquals(pm.getPlateType().getId(), pme.getPlateType().getId());

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
                .withId(300L)
                .withDescription("foo123")
                .withName("pme").withPlateType(plateTypeEntity);
        Coordinate coord = new Coordinate(4, 5);
        pme.getWells().put(
                coord,
                new WellMapEntity(coord)
                        .withId(800L)
                        .withLabel("ABC")
                        .withType(WellType.COMP)
        );
        return pme;
    }

    private WellMap makeWell() {
        return new WellMap(1, 2)
                .withId(1234L)
                .withType(WellType.COMP).withLabel("A");
    }

    private PlateMap makePlateMap() {
        return new PlateMap()
                .withName("plateMap")
                .withDescription("desc")
                .withId(100L)
                .withPlateType(plateType)
                .withWells(makeWell());
    }
}
