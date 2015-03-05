package edu.harvard.we99.services;

import edu.harvard.we99.domain.Plate;
import edu.harvard.we99.services.storage.ExperimentStorage;
import edu.harvard.we99.services.storage.PlateStorage;

import javax.ws.rs.core.Response;

/**
 * @author mford
 */
public class PlateServiceImpl implements PlateService {

    private final PlateStorage storage;
    private final ExperimentStorage experimentStorage;

    public PlateServiceImpl(PlateStorage storage, ExperimentStorage experimentStorage) {
        this.storage = storage;
        this.experimentStorage = experimentStorage;
    }

    @Override
    public Plate create(Long experimentId, Plate plate) {
        return experimentStorage.addPlate(experimentId, plate);
    }

    @Override
    public Plate get(Long experimentId, Long id) {
        return storage.get(id);
    }

    @Override
    public Plate update(Long experimentId, Long id, Plate plate) {
        return storage.update(id, plate);
    }

    @Override
    public Response delete(Long experimentId, Long id) {
        experimentStorage.removePlate(experimentId, id);
        return null;
    }
}
