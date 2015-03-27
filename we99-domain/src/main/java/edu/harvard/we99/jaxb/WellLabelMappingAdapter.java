package edu.harvard.we99.jaxb;

import edu.harvard.we99.domain.WellLabelMapping;

public class WellLabelMappingAdapter extends GenericMapAdapter<String, WellLabelMapping> {
    @Override
    protected String toKey(WellLabelMapping entity) {
        return entity.getLabel();
    }
}
