package edu.harvard.we99.domain;

import edu.harvard.we99.domain.results.PlateResult;
import edu.harvard.we99.domain.results.ResultStatus;
import edu.harvard.we99.domain.results.StatusChange;
import edu.harvard.we99.services.ExperimentService;
import edu.harvard.we99.services.PlateTypeService;
import edu.harvard.we99.services.experiments.PlateResource;
import edu.harvard.we99.services.experiments.PlateResultResource;
import edu.harvard.we99.services.experiments.PlatesResource;
import edu.harvard.we99.test.EastCoastTimezoneRule;
import edu.harvard.we99.test.Scrubbers;
import org.junit.Rule;
import org.junit.Test;

import javax.inject.Inject;
import javax.ws.rs.core.Response;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;

import static edu.harvard.we99.test.BaseFixture.assertJsonEquals;
import static edu.harvard.we99.test.BaseFixture.load;
import static edu.harvard.we99.test.BaseFixture.name;
import static edu.harvard.we99.test.BaseFixture.stream;
import static edu.harvard.we99.util.JacksonUtil.toJsonString;
import static org.junit.Assert.assertEquals;

/**
 * @author mford
 */
public class ResultServiceImplIT extends JpaSpringFixture {

    @Inject
    private ExperimentService experimentService;

    @Inject
    private PlateTypeService plateTypeService;

    @Rule
    public EastCoastTimezoneRule eastCoastTimezoneRule = new EastCoastTimezoneRule();

    @Rule
    public AuthenticatedUserRule authenticatedUserRule =
            new AuthenticatedUserRule("we99.2015@gmail.com", this);

    @Test
    public void removeWellResult() throws Exception {
        Experiment exp = experimentService.create(new Experiment(name("xp")));
        PlateType pt = plateTypeService.create(
                new PlateType()
                        .withName(name("PlateType"))
                        .withDim(new PlateDimension(5,5))
                        .withManufacturer(name("Man")));
        PlatesResource pr = experimentService.getExperiment(exp.getId()).getPlates();
        Plate plate = pr.create(
                new Plate()
                        .withName(name("Plate"))
                        .withWells(makeWells(5, 5))
                        .withPlateType(pt)
        );

        PlateResource plates = pr.getPlates(plate.getId());
        PlateResult plateResult = plates.getPlateResult().uploadResults(stream("/ResultServiceImplIT/results-single.csv"));

        Function<String, String> scrubber = Scrubbers.iso8601.andThen(Scrubbers.uuid).andThen(Scrubbers.pkey);
        // assert the results
        assertJsonEquals(load("/ResultServiceImplIT/all-results.json"),
                toJsonString(plateResult), scrubber);

        // drop a well
        PlateResultResource resultResource = plates.getPlateResult();
        Response resp = resultResource.updateStatus(
                new StatusChange(new Coordinate(0, 0), ResultStatus.EXCLUDED));
        assertOk(resp);

        // assert the results again
        PlateResult oneWellRemoved = resultResource.get();
        assertJsonEquals(load("/ResultServiceImplIT/one-removed.json"),
                toJsonString(oneWellRemoved), scrubber);

        // restore a well
        resp = resultResource.updateStatus(
                new StatusChange(new Coordinate(0,0), ResultStatus.INCLUDED));
        assertOk(resp);

        // assert the results again
        PlateResult allWellsBack = resultResource.get();
        assertJsonEquals(load("/ResultServiceImplIT/all-results.json"),
                toJsonString(allWellsBack), scrubber);
    }

    private void assertOk(Response resp) {
        assertEquals(200, resp.getStatus());
    }

    protected static Well[] makeWells(int rowCount, int colCount) {
        Set<Well> wells = new HashSet<>();

        for(int row=0; row< rowCount; row++) {
            for(int col=0; col< colCount; col++) {
                wells.add(
                        new Well(row, col)
                                .withType(WellType.EMPTY)
                );
            }
        }
        assertEquals(rowCount * colCount, wells.size());

        return wells.toArray(new Well[wells.size()]);
    }

}
