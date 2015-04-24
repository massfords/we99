package edu.harvard.we99.services;

import edu.harvard.we99.domain.*;
import edu.harvard.we99.domain.results.DoseResponseResult;
import edu.harvard.we99.services.experiments.DoseResponseResource;
import edu.harvard.we99.services.experiments.ExperimentResource;
import edu.harvard.we99.util.ClientFactory;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.multipart.ContentDisposition;
import org.apache.cxf.jaxrs.ext.multipart.MultipartBody;

import javax.ws.rs.core.Response;
import java.net.URL;
import java.util.*;

import static edu.harvard.we99.test.BaseFixture.name;
import static org.junit.Assert.assertEquals;

/**
 * @author alan orcharton
 */
public class DoseResponseResultsFixture {
    protected final ExperimentService experimentService;
    protected final CompoundService compoundService;
    protected final PlateType plateType;

    public DoseResponseResultsFixture() throws Exception {
        URL url = new URL(WebAppIT.WE99_URL);
        ClientFactory cf = new ClientFactory(url, WebAppIT.WE99_EMAIL, WebAppIT.WE99_PW);

        // install some plate types
        PlateTypeService plateTypeService = cf.create(PlateTypeService.class);
        plateType = plateTypeService.create(
                new PlateType()
                        .setDim(new PlateDimension(16, 24))
                        .setName(name("pt"))
                        .setManufacturer("Foo Inc.")
        );
        experimentService = cf.create(ExperimentService.class);
        compoundService = cf.create(CompoundService.class);
    }

    protected Experiment createExperiment() throws Exception {
        return experimentService.create(
                new Experiment(name("experiment")).setProtocol(new Protocol(name("p")))
        );
    }




    protected Plate createDoseResponseForCompound(Experiment experiment) {
        Double[] wellDoses = {0.00003,9.49E-06,3.00E-06,9.51E-07,3.01E-07,
                9.52E-08,3.01E-08,9.53E-09,3.02E-09,9.55E-10,3.02E-10};
        Compound c1 = new Compound().setName("SourPlums");
        Compound aCompound = compoundService.create(c1);
        ExperimentResource er = experimentService.getExperiment(experiment.getId());
        Plate plate = er.getPlates().create(new Plate(name("plate"), plateType).withWells(makeDoseCompoundWells(16,24,aCompound,wellDoses)));

        return plate;
    }


    private static Well[] makeDoseCompoundWells(int rowCount, int colCount, Compound c1, Double[] doses) {
        Set<Well> wells = new HashSet<>();

        Queue<Dose> doseQueue = new LinkedList<>();
        for (Double d: doses){
            Dose aDose = new Dose();
            aDose.setAmount(new Amount(d, DoseUnit.MICROMOLAR));
            aDose.setCompound(c1);
            doseQueue.add(aDose);

        }


        for (int row = 0; row < rowCount; row++) {
            for (int col = 0; col < colCount; col++) {
                if(col == 0){
                    wells.add(
                            new Well(row,col)
                                    .setType(WellType.POSITIVE)
                    );
                }
                else if (col == 1){
                    if(!doseQueue.isEmpty()) {
                        Dose d = (Dose) doseQueue.remove();
                        Set<Dose> ds = new HashSet<>();
                        ds.add(d);
                        wells.add(
                                new Well(row, col)
                                        .setType(WellType.COMP)
                                        .setContents(ds)

                        );
                    } else {
                        wells.add(
                                new Well(row, col)
                                        .setType(WellType.EMPTY)
                        );

                    }
                } else if(col == 23){
                    wells.add(
                            new Well(row, col)
                                    .setType(WellType.NEGATIVE)
                    );

                } else {
                    wells.add(
                            new Well(row, col)
                                    .setType(WellType.EMPTY)
                    );

                }
            }
        }
        assertEquals(rowCount * colCount, wells.size());
        return wells.toArray(new Well[wells.size()]);
    }

    protected Response postResults(DoseResponseResult doseResponseResult, String file) {
        String path = String.format("/experiment/%d/doseresponse/%d/results",
                doseResponseResult.getExperiment().getId(),
                doseResponseResult.getId());
        WebClient client = WebClient.create(WebAppIT.WE99_URL + path,
                WebAppIT.WE99_EMAIL, WebAppIT.WE99_PW, null);
        client.type("multipart/form-data");
        ContentDisposition cd = new ContentDisposition("attachment;filename=results.csv");
        Attachment att = new Attachment("file", getClass().getResourceAsStream(file), cd);
        Response response = client.post(new MultipartBody(att));
        assertEquals(200, response.getStatus());
        return response;
    }

    protected Response postPlateResults(Plate plate, String file) {
        String path = String.format("/experiment/%d/plates/%d/results",
                plate.getExperimentId(),
                plate.getId());
        WebClient client = WebClient.create(WebAppIT.WE99_URL + path,
                WebAppIT.WE99_EMAIL, WebAppIT.WE99_PW, null);
        client.type("multipart/form-data");
        ContentDisposition cd = new ContentDisposition("attachment;filename=results.csv");
        Attachment att = new Attachment("file", getClass().getResourceAsStream(file), cd);
        Response response = client.post(new MultipartBody(att));
        assertEquals(200, response.getStatus());
        return response;
    }



}


