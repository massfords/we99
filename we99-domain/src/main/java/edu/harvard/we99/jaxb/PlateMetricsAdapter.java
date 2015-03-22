package edu.harvard.we99.jaxb;

import edu.harvard.we99.domain.results.PlateMetrics;

/**
 * @author mford
 */
public class PlateMetricsAdapter extends GenericMapAdapter<String,PlateMetrics> {
    @Override
    protected String toKey(PlateMetrics entity) {
        return entity.getLabel();
    }
}
