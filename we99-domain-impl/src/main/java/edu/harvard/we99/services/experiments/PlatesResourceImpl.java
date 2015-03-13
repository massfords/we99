package edu.harvard.we99.services.experiments;

import edu.harvard.we99.domain.Experiment;
import edu.harvard.we99.domain.Plate;
import edu.harvard.we99.domain.lists.Plates;
import edu.harvard.we99.services.storage.PlateStorage;
import edu.harvard.we99.services.storage.ResultStorage;

/**
 * @author mford
 */
public class PlatesResourceImpl implements PlatesResource {

    private final Long experimentId;
    private final PlateStorage plateStorage;
    private final ResultStorage resultStorage;

    public PlatesResourceImpl(Long experimentId, PlateStorage plateStorage,
                              ResultStorage resultStorage) {
        this.experimentId = experimentId;
        this.plateStorage = plateStorage;
        this.resultStorage = resultStorage;
    }

    @Override
    public Plate create(Plate plate) {
        plate.setExperiment(new Experiment().withId(experimentId));
        return plateStorage.create(plate);
    }

    @Override
    public Plates list() {
        return new Plates(plateStorage.listAll(experimentId));
    }

    @Override
    public PlateResource getPlates(Long plateId) {
        return new PlateResourceImpl(experimentId, plateId,
                plateStorage, resultStorage);
    }
}
