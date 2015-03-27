package edu.harvard.we99.domain;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static edu.harvard.we99.test.BaseFixture.assertJsonEquals;
import static edu.harvard.we99.test.BaseFixture.load;
import static edu.harvard.we99.util.JacksonUtil.toJsonString;
import static org.assertj.core.util.Arrays.array;

/**
 * @author markford
 */
@RunWith(Parameterized.class)
public class WellLabelMappingTest {

    @Parameterized.Parameters
    public static List<Object[]> params() throws Exception {
        List<Object[]> params = new ArrayList<>();

        // 12 copies with 3 replicates
        params.add(
                array(
                new WellLabelMapping()
                .setCount(12)
                .setDilutionFactor(5d)
                .setLabel("A")
                .setReplicates(3)
                .setWellType(WellType.COMP)
                .setDose(new Dose(new Compound("Foo"),
                        new Amount(100d, DoseUnit.MICRO))),
                "/WellLabelMappingTest/evenNumber_replicates.json")
        );

        // 17 copies with 3 replicates so there are an uneven number of replicates
        params.add(
                array(
                        new WellLabelMapping()
                                .setCount(17)
                                .setDilutionFactor(5d)
                                .setLabel("A")
                                .setReplicates(3)
                                .setWellType(WellType.COMP)
                                .setDose(new Dose(new Compound("Foo"),
                                        new Amount(100d, DoseUnit.MICRO))),
                        "/WellLabelMappingTest/oddNumber_replicates.json")
        );

        // no replicates so we get 5 different dilutions
        params.add(
                array(
                        new WellLabelMapping()
                                .setCount(5)
                                .setDilutionFactor(5d)
                                .setLabel("A")
                                .setWellType(WellType.COMP)
                                .setDose(new Dose(new Compound("Foo"),
                                        new Amount(100d, DoseUnit.MICRO))),
                        "/WellLabelMappingTest/noReplicates.json")
        );

        // no dilution and no replicats, just the same value 5 times
        params.add(
                array(
                        new WellLabelMapping()
                                .setCount(5)
                                .setLabel("A")
                                .setWellType(WellType.COMP)
                                .setDose(new Dose(new Compound("Foo"),
                                        new Amount(100d, DoseUnit.MICRO))),
                        "/WellLabelMappingTest/noDilution_noReplicates.json")
        );

        // replicates but no dilution, still the same 5 values. Odd case, should
        // prob warn the user that they forgot the dilution?
        params.add(
                array(
                        new WellLabelMapping()
                                .setCount(5)
                                .setLabel("A")
                                .setReplicates(3)
                                .setWellType(WellType.COMP)
                                .setDose(new Dose(new Compound("Foo"),
                                        new Amount(100d, DoseUnit.MICRO))),
                        "/WellLabelMappingTest/noDilution.json")
        );

        return params;
    }

    private final WellLabelMapping wlm;
    private final String expected;

    public WellLabelMappingTest(WellLabelMapping wlm, String expectedPath) throws IOException {
        this.wlm = wlm;
        this.expected = load(expectedPath);
    }

    @Test
    public void test() throws Exception {
        wlm.initDoses();
        assertJsonEquals(expected, toJsonString(wlm.getDoses()));
    }
}
