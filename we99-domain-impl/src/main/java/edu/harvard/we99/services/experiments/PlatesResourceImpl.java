package edu.harvard.we99.services.experiments;

import edu.harvard.we99.domain.Experiment;
import edu.harvard.we99.domain.Plate;
import edu.harvard.we99.domain.lists.Plates;
import edu.harvard.we99.services.storage.PlateStorage;

/**
 * @author mford
 */
public abstract class PlatesResourceImpl implements PlatesResource {

    private Long experimentId;
    private final PlateStorage plateStorage;

    public PlatesResourceImpl(PlateStorage plateStorage) {
        this.plateStorage = plateStorage;
    }

    @Override
    public Plate create(Plate plate) {
        plate.setId(null);
        plate.setExperiment(new Experiment().withId(experimentId));
        return plateStorage.create(plate);
    }

    @Override
    public Plates list() {
        return new Plates(plateStorage.listAll(experimentId));
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
