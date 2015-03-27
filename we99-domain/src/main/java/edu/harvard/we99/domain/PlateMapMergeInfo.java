package edu.harvard.we99.domain;

import edu.harvard.we99.jaxb.WellLabelMappingAdapter;

import javax.annotation.Generated;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author markford
 */
public class PlateMapMergeInfo extends BaseEntity {

    private Long plateMapId;
    private PlateType plateType;
    private String plateName;

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

    @Generated("generated by IDE")
    public Long getPlateMapId() {
        return plateMapId;
    }

    @Generated("generated by IDE")
    public PlateMapMergeInfo setPlateMapId(Long plateMapId) {
        this.plateMapId = plateMapId;
        return this;
    }

    @Generated("generated by IDE")
    public String getPlateName() {
        return plateName;
    }

    @Generated("generated by IDE")
    public PlateMapMergeInfo setPlateName(String plateName) {
        this.plateName = plateName;
        return this;
    }

    @Generated("generated by IDE")
    public PlateType getPlateType() {
        return plateType;
    }

    @Generated("generated by IDE")
    public PlateMapMergeInfo setPlateType(PlateType plateType) {
        this.plateType = plateType;
        return this;
    }
}