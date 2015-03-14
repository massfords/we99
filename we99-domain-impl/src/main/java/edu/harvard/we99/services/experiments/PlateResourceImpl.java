package edu.harvard.we99.services.experiments;

import edu.harvard.we99.domain.Experiment;
import edu.harvard.we99.domain.Plate;
import edu.harvard.we99.domain.results.PlateResult;
import edu.harvard.we99.services.io.PlateResultCSVReader;
import edu.harvard.we99.services.storage.PlateStorage;
import edu.harvard.we99.services.storage.ResultStorage;
import org.apache.commons.io.IOUtils;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;

/**
 * @author mford
 */
public class PlateResourceImpl implements PlateResource {

    private final Long experimentId;
    private final Long plateId;
    private final PlateStorage plateStorage;
    private final ResultStorage resultStorage;

    public PlateResourceImpl(Long experimentId, Long plateId,
                             PlateStorage plateStorage,
                             ResultStorage resultStorage) {
        this.plateId = plateId;
        this.experimentId = experimentId;
        this.plateStorage = plateStorage;
        this.resultStorage = resultStorage;
    }

    @Override
    public Plate get() {
        return plateStorage.get(plateId);
    }

    @Override
    public Plate update(Plate plate) {
        return plateStorage.update(plateId, plate);
    }

    @Override
    public Response delete() {
        plateStorage.delete(plateId);
        return Response.ok().build();
    }

    @Override
    public PlateResult uploadResults(InputStream csv) {
        // get the plate w/ the given id
        // read the csv into a PlateResult
        // assign the Plate / Experiment to the PlateResult
        // persist all to the storage

        String source;
        try {
            source = IOUtils.toString(csv);
        } catch (IOException e) {
            throw new WebApplicationException(Response.serverError().build());
        }

        PlateResultCSVReader reader = new PlateResultCSVReader();
        PlateResult pr = reader.read(new BufferedReader(new StringReader(source)));
        pr.setPlate(new Plate().withId(plateId).withExperiment(new Experiment().withId(experimentId)));
        pr.setSource(source);
        return resultStorage.create(pr);
    }

    @Override
    public PlateResultsResource getResults() {
        return new PlateResultsResourceImpl(experimentId, plateId, resultStorage);
    }
}