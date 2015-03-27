package edu.harvard.we99.services;

import edu.harvard.we99.domain.PlateDimension;
import edu.harvard.we99.domain.PlateType;
import edu.harvard.we99.test.Scrubbers;
import edu.harvard.we99.util.ClientFactory;
import org.apache.commons.io.IOUtils;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import javax.ws.rs.core.Response;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static edu.harvard.we99.test.BaseFixture.assertJsonEquals;
import static edu.harvard.we99.test.BaseFixture.load;
import static edu.harvard.we99.test.BaseFixture.name;
import static org.assertj.core.util.Arrays.array;

/**
 * @author mford
 */
@RunWith(Parameterized.class)
public class PlateMapImportST {

    @Parameterized.Parameters
    public static List<Object[]> params() throws Exception {
        List<Object[]> params = new ArrayList<>();
        params.add(array("/PlateMapIT/input5x5.csv", "/PlateMapIT/expected-input-pm5x5.json"));
        params.add(array("/PlateMapIT/input50x50.csv", "/PlateMapIT/expected-input-pm50x50.json"));
        // test that the a CSV with a row,col value of 50,x or x,50 doesn't include
        // the dimension of 50x50 since the coordinates are zero based so a value
        // of '50' indicates that there are at least 51 values for that dimension
        params.add(array("/PlateMapIT/input51x51.csv", "/PlateMapIT/expected-input-pm51x51.json"));
        // test for no plate types coming back
        params.add(array("/PlateMapIT/input200x200.csv", "/PlateMapIT/expected-input-pm200x200.json"));
        return params;
    }

    @BeforeClass
    public static void init() throws Exception {
        URL url = new URL(WebAppIT.WE99_URL);
        ClientFactory cf = new ClientFactory(url, WebAppIT.WE99_EMAIL, WebAppIT.WE99_PW);

        // install some plate types
        PlateTypeService plateTypeService = cf.create(PlateTypeService.class);
        for(int i=10; i<100; i+=10) {
            plateTypeService.create(
                new PlateType()
                    .withDim(new PlateDimension(i, i))
                    .withName(name("pt"))
                    .withManufacturer("Foo Inc.")
            );
        }
    }

    private final String input;
    private final String expected;

    public PlateMapImportST(String input, String expected) {
        this.input = input;
        this.expected = expected;
    }

    @Test
    public void createFromUpload() throws Exception {
        Response response = PlateMapClientFixture.upload(input, name("platemap"));
        InputStream is = (InputStream) response.getEntity();
        String json = IOUtils.toString(is);
        assertJsonEquals(load(expected), json, Scrubbers.uuid.andThen(Scrubbers.pkey));
    }
}
