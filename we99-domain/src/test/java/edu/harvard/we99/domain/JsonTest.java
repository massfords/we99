package edu.harvard.we99.domain;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static edu.harvard.we99.test.BaseFixture.assertJsonEquals;
import static edu.harvard.we99.test.BaseFixture.load;
import static edu.harvard.we99.util.JacksonUtil.fromString;
import static edu.harvard.we99.util.JacksonUtil.toJsonString;
import static org.assertj.core.util.Arrays.array;
import static org.junit.Assert.assertNotNull;

/**
 * Simple tests to ensure that the payloads marshall/unmarshall as expected
 *
 * @author mford
 */
@RunWith(Parameterized.class)
public class JsonTest {

    @Parameterized.Parameters
    public static List<Object[]> params() throws Exception {
        List<Object[]> params = new ArrayList<>();

        params.add(
                array(
                        new Compound("h20").setId(1234L),
                load("/JsonTest/compound.json")
                ));

        params.add(
                array(new Dose()
                                .setId(1234L)
                                .setAmount(new Amount(2d, DoseUnit.MILLIMOLAR))
                                .setCompound(new Compound("h20")),
                        load("/JsonTest/dose.json")
                ));

        params.add(
                array(
                        new Plate("plate1", type())
                                .setBarcode("1234"),
                        load("/JsonTest/plate-no-wells.json")
                ));

        params.add(
                array(
                        new Plate("plate1", type())
                                .setBarcode("1234")
                                .setWells(wells(2, 3))
                        ,
                        load("/JsonTest/plate-wells.json")
                ));

        params.add(array(
                new PlateType().setDim(new PlateDimension(3, 4)).setManufacturer("Foo Inc."),
                load("/JsonTest/plateType.json")
        ));

        params.add(array(
                new PlateMapMergeInfo()
                        .add(new WellLabelMapping()
                                .setLabel("A")
                                .setCount(12)
                                .setDilutionFactor(5d)
                                .setDose(new Dose(new Compound("Ammonia"), new Amount(100, DoseUnit.MICROMOLAR)))
                                .setReplicates(2)
                                .setWellType(WellType.COMP))
                        .add(new WellLabelMapping()
                                .setLabel("B")
                                .setCount(12)
                                .setDilutionFactor(5d)
                                .setDose(new Dose(new Compound("Nickel chloride"), new Amount(50, DoseUnit.MICROMOLAR)))
                                .setReplicates(2)
                                .setWellType(WellType.COMP))
                        .add(new WellLabelMapping()
                                .setLabel("C")
                                .setCount(12)
                                .setDilutionFactor(5d)
                                .setDose(new Dose(new Compound("Potassium bromate"), new Amount(50, DoseUnit.MICROMOLAR)))
                                .setReplicates(3)
                                .setWellType(WellType.COMP))
                        .add(new WellLabelMapping()
                                .setLabel("Positive")
                                .setCount(12)
                                .setDilutionFactor(5d)
                                .setDose(new Dose(new Compound("xpf54"), new Amount(50, DoseUnit.MICROMOLAR)))
                                .setWellType(WellType.POSITIVE))
                        .add(new WellLabelMapping()
                                .setLabel("Negative")
                                .setCount(12)
                                .setDilutionFactor(5d)
                                .setDose(new Dose(new Compound("brine"), new Amount(50, DoseUnit.MICROMOLAR)))
                                .setReplicates(3)
                                .setWellType(WellType.NEGATIVE))
                ,
                load("/JsonTest/mergeInfo.json")));

        return params;
    }

    private final String expected;
    private final Object bean;

    public JsonTest(Object bean, String expected) {
        this.bean = bean;
        this.expected = expected;
    }

    @Test
    public void test() throws Exception {

        String json = toJsonString(bean);

        assertJsonEquals(this.expected, json);

        // make sure we can read it back
        Object o = fromString(json, bean.getClass());
        assertNotNull(o);
    }

    private static PlateType type() {
        return new PlateType()
                .setDim(new PlateDimension(3,4))
                .setManufacturer("Foo Inc.");
    }

    private static Map<Coordinate,Well> wells(int rows, int cols) {
        Well[] wells = new Well[rows*cols];
        for(int row = 0; row<rows; row++) {
            for(int col=0; col<cols; col++) {
                wells[cols*row+col] = new Well(row, col)
                        .setLabels(Collections.singleton(new Label("loc", "well " + row + "," + col)))
                        .setType(WellType.EMPTY);
            }
        }
        Map<Coordinate, Well> map = new LinkedHashMap<>();
        for(Well w : wells) {
            map.put(w.getCoordinate(), w);
        }
        return map;
    }
}
