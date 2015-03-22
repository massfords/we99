package edu.harvard.we99.services.storage.entities;

import edu.harvard.we99.domain.Coordinate;
import edu.harvard.we99.domain.PlateDimension;
import edu.harvard.we99.domain.WellType;
import edu.harvard.we99.domain.results.PlateResult;
import edu.harvard.we99.domain.results.ResultStatus;
import edu.harvard.we99.test.EastCoastTimezoneRule;
import org.joda.time.DateTime;
import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static edu.harvard.we99.test.BaseFixture.assertJsonEquals;
import static edu.harvard.we99.test.BaseFixture.load;
import static edu.harvard.we99.util.JacksonUtil.toJsonString;

/**
 * @author mford
 */
public class PlateResultMapperTest {

    @Rule
    public EastCoastTimezoneRule eastCoastTimezoneRule = new EastCoastTimezoneRule();

    private final PlateTypeEntity plateTypeEntity =
            new PlateTypeEntity().withId(250L)
                    .withDim(new PlateDimension(5, 5))
                    .withName("foo").withManufacturer("Man1");

    @Test
    public void entityToDomain() throws Exception {
        List<SampleEntity> samples = new ArrayList<>();
        samples.add(new SampleEntity()
                .withId(12L)
                .withComments("sample comments")
                .withLabel("ABCD")
                .withMeasuredAt(new DateTime("2015-02-03T04:05:30"))
                .withStatus(ResultStatus.EXCLUDED)
                .withValue(123.456D));

        Map<Coordinate, WellResultsEntity> resultsMap = new HashMap<>();

        long id=0;
        for(int row=0; row<plateTypeEntity.getDim().getRows(); row++) {
            for(int col=0; col<plateTypeEntity.getDim().getCols(); col++) {
                Coordinate coord = new Coordinate(row, col);
                resultsMap.put(coord,
                        new WellResultsEntity()
                                .withId(id++)
                                .withCoordinate(coord)
                                .withResultStatus(ResultStatus.INCLUDED)
                                .withSamples(samples));
            }
        }

        PlateResultEntity pre = new PlateResultEntity()
                .withId(100L)
                .withComments("comments")
                .withCreated(new DateTime("2015-01-02T10:20:30"))
                .withSource("source should be ignored")
                .withPlate(makePlateEntity())
                .withWellResults(resultsMap)
                ;
        PlateResult pr = Mappers.PLATERESULT.map(pre);
        assertJsonEquals(load("/Mappers/plateResult.json"), toJsonString(pr));
    }

    private PlateEntity makePlateEntity() {
        PlateEntity plateEntity = new PlateEntity()
                .withId(300L)
                .withBarcode("456D")
                .withDescription("foo123")
                .withName("pe").withPlateType(plateTypeEntity);

        Map<Coordinate,WellEntity> wells = new HashMap<>();
        for(int row=0; row<plateEntity.getPlateType().getDim().getRows(); row++) {
            for(int col=0; col<plateEntity.getPlateType().getDim().getCols(); col++) {
                wells.put(new Coordinate(row, col), new WellEntity(row,col).withType(WellType.COMP));
            }
        }
        plateEntity.setWells(wells);

        return plateEntity;
    }

}
