package edu.harvard.we99.services.storage.entities;

import edu.harvard.we99.domain.*;
import edu.harvard.we99.domain.results.DoseResponseResult;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * @author alan orcharton
 */
public class ExperimentPointMapperTest {

    private final PlateType plateType = new PlateType().setId(250L)
            .setDim(new PlateDimension(10, 20))
            .setName("foo").setManufacturer("Man1");

    private final PlateTypeEntity plateTypeEntity =
            new PlateTypeEntity().setId(250L)
                    .setDim(new PlateDimension(10, 20))
                    .setName("foo").setManufacturer("Man1");

    private final Compound compound = new Compound(500L, "comp1");

    private final DoseResponseResult drResult = new DoseResponseResult()
            .setCompound(compound)
            .setId(302L)
            .setComments("Dose response for comp1");



    private static final double DELTA  = 1E-8;




    @Test
    public void domainToEntity_new() throws Exception {
        Well well = makeWell();
        Plate plate = makePlate(well);


        ExperimentPoint fromCaller = new ExperimentPoint()
                .setAssociatedDoseResponseResult(drResult)
                .setAssociatedPlate(plate)
                .setAssociatedWell(well)
                .setId(501L)
                .setX(new Double(2.0))
                .setY(5.0);

        ExperimentPointEntity epe = Mappers.EXPERIMENTPOINT.mapReverse(fromCaller);
        //id is copied over
        assertEquals(501L, epe.getId().longValue());
        //fields not copied over
        assertNull(epe.getAssociatedPlate());
        assertNull(epe.getAssociatedWell());
        assertNull(epe.getAssociatedDoseResponseResult());
        //description is copied over

        assertEquals(5.0,epe.getY(),DELTA);
        assertEquals(2.0,epe.getX(),DELTA);



    }


    @Test
    public void domainToEntity_existing() throws Exception {
        Well well = makeWell();
        Plate plate = makePlate(well);

        ExperimentPointEntity epe = new ExperimentPointEntity()
                .setId(501L)
                .setAssociatedPlate(makePlateEntity())
                .setX(4.5)
                .setY(5.2);

        ExperimentPoint fromCaller = new ExperimentPoint()
                .setAssociatedDoseResponseResult(drResult)
                .setAssociatedPlate(plate)
                .setAssociatedWell(well)
                .setId(501L)
                .setX(new Double(2.0))
                .setY(5.0);



        Mappers.EXPERIMENTPOINT.mapReverse(fromCaller,epe);
        assertEquals(fromCaller.getId(), epe.getId());
        assertEquals(fromCaller.getX(), epe.getX());
        assertEquals(fromCaller.getY(), epe.getY());
        assertEquals(300L, epe.getAssociatedPlate().getId().longValue());

    }



    private PlateEntity makePlateEntity() {
        return new PlateEntity()
                .setId(300L)
                .setBarcode("456D")
                .setDescription("foo123")
                .setName("pe").setPlateType(plateTypeEntity);
    }

    private Plate makePlate(Well well) {
        return new Plate()
                .setId(100L)
                .setBarcode("abc123")
                .setPlateType(plateType)
                .setDescription("desc")
                .setName("plate1")
                .withWells(well);
    }

    private Well makeWell() {
        return new Well(1, 2)
                .setId(1234L)
                .setType(WellType.COMP)
                .dose(
                        new Dose()
                                .setId(501L)
                                .setCompound(compound)
                                .setAmount(new Amount(10, DoseUnit.MICROMOLAR))
                );
    }
}
