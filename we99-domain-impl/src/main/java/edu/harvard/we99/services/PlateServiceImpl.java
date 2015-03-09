package edu.harvard.we99.services;

import edu.harvard.we99.domain.Plate;
import edu.harvard.we99.domain.lists.Plates;
import edu.harvard.we99.security.UserContextProvider;
import edu.harvard.we99.services.storage.ExperimentStorage;
import edu.harvard.we99.services.storage.PlateStorage;

import javax.ws.rs.core.Response;

/**
 * @author mford
 */
public class PlateServiceImpl implements PlateService {

    private final PlateStorage storage;
    private final ExperimentStorage experimentStorage;
    private final UserContextProvider ucp;

    public PlateServiceImpl(UserContextProvider ucp, PlateStorage storage,
                            ExperimentStorage experimentStorage) {
        this.storage = storage;
        this.experimentStorage = experimentStorage;
        this.ucp = ucp;
    }

    @Override
    public Plate create(Long experimentId, Plate plate) {
        ucp.assertCallerIsMember(experimentId);
        return experimentStorage.addPlate(experimentId, plate);
    }

    @Override
    public Plate get(Long experimentId, Long id) {
        ucp.assertCallerIsMember(experimentId);
        return storage.get(id);
    }

    @Override
    public Plate update(Long experimentId, Long id, Plate plate) {
        ucp.assertCallerIsMember(experimentId);
        return storage.update(id, plate);
    }

    @Override
    public Response delete(Long experimentId, Long id) {
        ucp.assertCallerIsMember(experimentId);
        experimentStorage.removePlate(experimentId, id);
        return null;
    }

    @Override
    public Plates list(Long experimentId) {
        ucp.assertCallerIsMember(experimentId);
        return new Plates(storage.listAll(experimentId));
    }
}
