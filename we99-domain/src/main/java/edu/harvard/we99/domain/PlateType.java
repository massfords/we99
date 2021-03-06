package edu.harvard.we99.domain;

import javax.annotation.Generated;
import javax.validation.constraints.NotNull;

/**
 * Defines the type of physical plate we're dealing with. These plates are
 * assumed to be rectangular. We can infer the shape and size from the rows/cols.
 *
 * @author mford
 */
public class PlateType extends BaseEntity {
    private Long id;

    /**
     * name of the plate
     */
    @NotNull
    private String name;

    /**
     * Optional description for the plate.
     */
    private String description;

    @NotNull
    private PlateDimension dim;

    /**
     * The manufacturer of the plate
     */
    @NotNull
    private String manufacturer;

    /**
     * Link to the manufacturer's website to order plates of this type.
     */
    private String orderLink;

    private Long plateCount;

    @Generated("generated by IDE")
    public Long getId() {
        return id;
    }

    @Generated("generated by IDE")
    public PlateType setId(Long id) {
        this.id = id;
        return this;
    }

    @Generated("generated by IDE")
    public String getName() {
        return name;
    }

    @Generated("generated by IDE")
    public PlateType setName(String name) {
        this.name = name;
        return this;
    }

    @Generated("generated by IDE")
    public String getDescription() {
        return description;
    }

    @Generated("generated by IDE")
    public PlateType setDescription(String description) {
        this.description = description;
        return this;
    }

    @Generated("generated by IDE")
    public PlateDimension getDim() {
        return dim;
    }

    @Generated("generated by IDE")
    public PlateType setDim(PlateDimension dim) {
        this.dim = dim;
        return this;
    }

    @Generated("generated by IDE")
    public String getManufacturer() {
        return manufacturer;
    }

    @Generated("generated by IDE")
    public PlateType setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
        return this;
    }

    @Generated("generated by IDE")
    public String getOrderLink() {
        return orderLink;
    }

    @Generated("generated by IDE")
    public PlateType setOrderLink(String orderLink) {
        this.orderLink = orderLink;
        return this;
    }

    @Generated("generated by IDE")
    public Long getPlateCount() {
        return plateCount;
    }

    @Generated("generated by IDE")
    public PlateType setPlateCount(Long plateCount) {
        this.plateCount = plateCount;
        return this;
    }
}
