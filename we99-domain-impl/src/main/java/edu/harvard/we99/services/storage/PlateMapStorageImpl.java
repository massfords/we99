package edu.harvard.we99.services.storage;

import edu.harvard.we99.domain.PlateMap;
import edu.harvard.we99.domain.WellMap;

/**
 * Implementation of the PlanTemplateStorage
 *
 * @author mford
 */
public class PlateMapStorageImpl extends CRUDStorageImpl<PlateMap> implements CRUDStorage<PlateMap> {

    public PlateMapStorageImpl() {
        super(PlateMap.class);
    }

    @Override
    protected void updateFromCaller(PlateMap fromDb, PlateMap fromUser) {
        fromDb.withName(fromUser.getName())
                .withDescription(fromUser.getDescription());

        // update the wells
        fromDb.getWells().clear();
        fromDb.getWells().putAll(fromUser.getWells());
        for(WellMap well : fromDb.getWells().values()) {
            if (well.getId() == null) {
                em.persist(well);
            } else {
                em.merge(well);
            }
        }
    }
}
