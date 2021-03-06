package edu.harvard.we99.security;

import edu.harvard.we99.domain.BaseEntity;

import javax.annotation.Generated;
import javax.validation.constraints.NotNull;

/**
 * Models a permission in the system. The separation of Roles and Permissions
 * means a more flexible security configuration as system admins could conceivably
 * customize the permissions mapped to a given role. This allows the services
 * to have statically permission requirements while the Roles themselves can
 * be dynamic.
 *
 * @author mford
 */
public class Permission extends BaseEntity {
    private Long id;

    @NotNull
    private String name;

    public Permission() {}

    public Permission(String name) {
        this.name = name;
    }

    @Generated("generated by IDE")
    public Long getId() {
        return id;
    }

    @Generated("generated by IDE")
    public Permission setId(Long id) {
        this.id = id;
        return this;
    }

    @Generated("generated by IDE")
    public String getName() {
        return name;
    }

    @Generated("generated by IDE")
    public Permission setName(String name) {
        this.name = name;
        return this;
    }
}
