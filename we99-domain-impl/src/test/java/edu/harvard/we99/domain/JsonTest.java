package edu.harvard.we99.domain;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.ArrayList;
import java.util.List;

import static edu.harvard.we99.test.BaseFixture.array;
import static edu.harvard.we99.test.BaseFixture.assertJsonEquals;
import static edu.harvard.we99.test.BaseFixture.load;
import static edu.harvard.we99.util.JacksonUtil.fromString;
import static edu.harvard.we99.util.JacksonUtil.toJsonString;
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
                new Compound("h20").withId(1234L),
                load("/JsonTest/compound.json")
                ));

        params.add(
                array(new Dose()
                                .withId(1234L)
                                .withAmount(2, DoseUnit.MILLIS)
                                .withCompound(new Compound("h20")),
                        load("/JsonTest/dose.json")
                ));

        params.add(
                array(
                        new Plate("plate1", type())
                                .withBarcode(1234L)
                                .withDerivedFrom(456L),
                        load("/JsonTest/plate-no-wells.json")
                ));

        params.add(
                array(
                        new Plate("plate1", type())
                                .withBarcode(1234L)
                                .withDerivedFrom(456L)
                                .withWells(wells(2,3))
                        ,
                        load("/JsonTest/plate-wells.json")
                ));

        params.add(array(
                new PlateType().withDim(new PlateDimension(3, 4)).withManufacturer("Foo Inc."),
                load("/JsonTest/plateType.json")
        ));

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
                .withDim(new PlateDimension(3,4))
                .withManufacturer("Foo Inc.");
    }

    private static Well[] wells(int rows, int cols) {
        Well[] wells = new Well[rows*cols];
        for(int row = 0; row<rows; row++) {
            for(int col=0; col<cols; col++) {
                wells[cols*row+col] = new Well(row, col)
                        .withLabel("well " + row + "," + col)
                        .withType(WellType.EMPTY);
            }
        }
        return wells;
    }
}
