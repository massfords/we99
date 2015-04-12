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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
                        .setDim(new PlateDimension(10, 10))
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


    protected Plate createPlate(Experiment experiment) {
        Compound c1 = new Compound("SourPlums");
        compoundService.create(c1);
        ExperimentResource er = experimentService.getExperiment(experiment.getId());
        return er.getPlates().create(new Plate(name("plate"), plateType).withWells(makeDoseCompoundWells(plateType, c1)));
    }


    protected Plate createDoseResponseForCompound(Experiment experiment) {
        Compound c1 = new Compound().setName("SourPlums");
        Compound aCompound = compoundService.create(c1);
        ExperimentResource er = experimentService.getExperiment(experiment.getId());
        Plate plate = er.getPlates().create(new Plate(name("plate"), plateType).withWells(makeDoseCompoundWells(plateType, aCompound)));
        //List<Plate> plates = new ArrayList<>();
        //plates.add(plate);
        DoseResponseResult doseResponseResult = er.getDoseResponses().create();
        //DoseResponseResult doseResponseResult =er.getDoseResponses().createForCompound(c1, plates);
        //DoseResponseResult doseResponseResult2 = er.getDoseResponses().
        return plate;
    }


    private Well[] makeDoseCompoundWells(PlateType plateType, Compound compound) {
        List<Well> wells = new ArrayList<>();

        Dose dose1 = new Dose()
                .setCompound(compound)
                .setAmount(new Amount(575, DoseUnit.MICROMOLAR));
        Set<Dose> set1 = new HashSet<>();
        set1.add(dose1);

        for(int row=0; row<plateType.getDim().getRows(); row++) {
            for(int col=0; col<plateType.getDim().getCols(); col++) {
                wells.add(new Well(row, col).setType(WellType.COMP)
                        .withLabel("A", "A")
                        .setContents(set1));


            }
        }

        Well[] array = new Well[wells.size()];
        return wells.toArray(array);
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


