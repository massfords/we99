package edu.harvard.we99.services.experiments;

import edu.harvard.we99.domain.Compound;
import edu.harvard.we99.domain.Experiment;
import edu.harvard.we99.domain.Plate;
import edu.harvard.we99.domain.lists.Plates;
import edu.harvard.we99.services.io.PlateCSVReader;
import edu.harvard.we99.services.storage.CompoundStorage;
import edu.harvard.we99.services.storage.PlateStorage;
import edu.harvard.we99.services.storage.PlateTypeStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * @author mford
 */
public abstract class PlatesResourceImpl implements PlatesResource {

    private static final Logger log = LoggerFactory.getLogger(PlatesResourceImpl.class);

    private Long experimentId;
    private final PlateStorage plateStorage;
    private final CompoundStorage compoundStorage;
    private final PlateTypeStorage plateTypeStorage;

    public PlatesResourceImpl(PlateStorage plateStorage, CompoundStorage compoundStorage,
                              PlateTypeStorage plateTypeStorage) {
        this.plateStorage = plateStorage;
        this.compoundStorage = compoundStorage;
        this.plateTypeStorage = plateTypeStorage;
    }

    @Override
    public Plate create(Plate plate) {
        plate.setId(null);
        plate.setExperiment(new Experiment().withId(experimentId));
        return plateStorage.create(plate);
    }

    @Override
    public Plate create(String name, String plateTypeName, InputStream csv) {
        PlateCSVReader reader = new PlateCSVReader();
        try (Reader r = new BufferedReader(new InputStreamReader(csv))) {
            if (name == null) {
                name = UUID.randomUUID().toString();
            }
            Plate plate = reader.read(r).withName(name);
            plate.setExperiment(new Experiment().withId(experimentId));
            plate.setPlateType(plateTypeStorage.getByName(plateTypeName));
            // walk all of the compounds to set their id's or leave null if we're persisting
            Set<Compound> compounds = new HashSet<>();
            plate.getWells().values().forEach(well -> well.getContents().forEach(d -> compounds.add(d.getCompound())));
            Map<Compound, Long> resolvedIds = compoundStorage.resolveIds(compounds);
            plate.getWells().values().forEach(well -> well.getContents().forEach(d -> d.getCompound().setId(resolvedIds.get(d.getCompound()))));
            Plate created = plateStorage.create(plate);
            return created;
        } catch (IOException e) {
            log.error("error parsing csv", e);
            throw new WebApplicationException(Response.status(409).build());
        }
    }

    @Override
    public Plates list(Integer page) {
        return plateStorage.listAll(experimentId, page);
    }

    @Override
    public PlateResource getPlates(Long plateId) {
        PlateResource pr = createPlateResource();
        pr.setExperimentId(experimentId);
        pr.setPlateId(plateId);
        return pr;
    }

    protected abstract PlateResource createPlateResource();

    @Override
    public Long getId() {
        return experimentId;
    }

    @Override
    public void setId(Long id) {
        this.experimentId = id;
    }
}
