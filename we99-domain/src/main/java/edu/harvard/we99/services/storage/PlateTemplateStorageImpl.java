package edu.harvard.we99.services.storage;

import edu.harvard.we99.domain.Dose;
import edu.harvard.we99.domain.PlateTemplate;
import edu.harvard.we99.domain.Well;

/**
 * Implementation of the PlanTemplateStorage
 *
 * @author mford
 */
public class PlateTemplateStorageImpl extends CRUDStorageImpl<PlateTemplate> implements CRUDStorage<PlateTemplate> {

    public PlateTemplateStorageImpl() {
        super(PlateTemplate.class);
    }

    @Override
    protected void updateFromCaller(PlateTemplate fromDb, PlateTemplate fromUser) {
        fromDb.withName(fromUser.getName())
                .withDescription(fromUser.getDescription());

        // update the wells
        fromDb.getWells().clear();
        fromDb.getWells().putAll(fromUser.getWells());
        for(Well well : fromDb.getWells().values()) {
            if (well.getId() == null) {
                em.persist(well);
            } else {
                em.merge(well);
            }
            for(Dose d : well.getContents()) {
                d.setWell(well);
                if (d.getId() == null) {
                    em.persist(d);
                } else {
                    em.merge(d);
                }
            }
        }
    }
}
