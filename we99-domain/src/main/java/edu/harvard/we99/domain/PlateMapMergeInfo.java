package edu.harvard.we99.domain;

import edu.harvard.we99.jaxb.WellLabelMappingAdapter;

import javax.annotation.Generated;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author markford
 */
public class PlateMapMergeInfo extends BaseEntity{
    @XmlJavaTypeAdapter(WellLabelMappingAdapter.class)
    private Map<String, WellLabelMapping> mappings = new LinkedHashMap<>();

    public PlateMapMergeInfo add(WellLabelMapping mapping) {
        mappings.put(mapping.getLabel(), mapping);
        return this;
    }

    @Generated("generated by IDE")
    public Map<String, WellLabelMapping> getMappings() {
        return mappings;
    }

    @Generated("generated by IDE")
    public PlateMapMergeInfo setMappings(Map<String, WellLabelMapping> mappings) {
        this.mappings = mappings;
        return this;
    }
}
