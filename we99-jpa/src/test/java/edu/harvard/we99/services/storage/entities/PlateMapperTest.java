package edu.harvard.we99.services.storage.entities;

import edu.harvard.we99.domain.Amount;
import edu.harvard.we99.domain.Compound;
import edu.harvard.we99.domain.Coordinate;
import edu.harvard.we99.domain.Dose;
import edu.harvard.we99.domain.DoseUnit;
import edu.harvard.we99.domain.Plate;
import edu.harvard.we99.domain.PlateDimension;
import edu.harvard.we99.domain.PlateType;
import edu.harvard.we99.domain.Well;
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
public class PlateMapperTest {

    private final PlateType plateType = new PlateType().withId(250L)
            .withDim(new PlateDimension(10, 20))
            .withName("foo").withManufacturer("Man1");

    private final PlateTypeEntity plateTypeEntity =
            new PlateTypeEntity().withId(250L)
            .withDim(new PlateDimension(10, 20))
            .withName("foo").withManufacturer("Man1");

    private final Compound compound = new Compound(500L, "comp1");

    @Test
    public void domainToEntity_new() throws Exception {
        Well well = makeWell();
        Plate plate = makePlate(well);
        PlateEntity pe = Mappers.PLATES.mapReverse(plate);

        assertEquals(plate.getId(), pe.getId());
        assertEquals("abc123", pe.getBarcode());
        assertSame(plateType.getDim(), pe.getPlateType().getDim());
        assertSame(plateType.getId(), pe.getPlateType().getId());
        assertEquals("desc", pe.getDescription());
        assertEquals("plate1", pe.getName());
        assertEquals(0, pe.getWells().size());
    }

    @Test
    public void domainToEntity_existing() throws Exception {

        PlateEntity pe = makePlateEntity();

        Map<Coordinate, WellEntity> wells = pe.getWells();

        Well well = makeWell();
        Plate plate = makePlate(well);

        Mappers.PLATES.mapReverse(plate, pe);
        assertEquals(plate.getId(), pe.getId());
        assertEquals("abc123", pe.getBarcode());
        assertSame(plateType.getDim(), pe.getPlateType().getDim());
        assertSame(plateType.getId(), pe.getPlateType().getId());
        assertEquals("desc", pe.getDescription());
        assertEquals("plate1", pe.getName());

        // we're going to map the wells manually so the entity's wells shouldn't
        // have changed.
        assertSame(wells, pe.getWells());

    }

    @Test
    public void entityToDomain() throws Exception {
        PlateEntity pe = makePlateEntity();
        Coordinate coord = new Coordinate(1, 2);
        pe.getWells().put(coord, new WellEntity()
                .withId(1234L)
                .withType(WellType.COMP)
                .withLabel("A")
                .withCoordinate(new Coordinate(1, 2)));
        Plate map = Mappers.PLATES.map(pe);
        assertJsonEquals(load("/Mappers/plate.json"), toJsonString(map));
    }

    private PlateEntity makePlateEntity() {
        return new PlateEntity()
                .withId(300L)
                .withBarcode("456D")
                .withDescription("foo123")
                .withName("pe").withPlateType(plateTypeEntity);
    }

    private Plate makePlate(Well well) {
        return new Plate()
                .withId(100L)
                .withBarcode("abc123")
                .withPlateType(plateType)
                .withDescription("desc")
                .withName("plate1")
                .withWells(well);
    }

    private Well makeWell() {
        return new Well(1, 2)
                .withId(1234L)
                .withType(WellType.COMP)
                .dose(
                        new Dose()
                                .withId(300L)
                                .withCompound(compound)
                                .withAmount(new Amount(10, DoseUnit.NANO))
                );
    }
}
