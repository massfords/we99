package edu.harvard.we99.security;

import edu.harvard.we99.domain.BaseEntity;

import javax.annotation.Generated;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.MapKey;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.Map;
import java.util.TreeMap;

/**
 * A Role is a grouping of permissions. A user has a single role and the permissions
 * within the role determine what the user is allowed to do.
 *
 * @author mford
 */
@Entity
@Table(uniqueConstraints = {@UniqueConstraint( columnNames = {"name"})})
public class Role extends BaseEntity {

    /**
     * Primary key is generated
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(updatable = false)
    @Embedded
    private RoleName name;

    @ManyToMany(fetch = FetchType.EAGER)
    @MapKey(name="name")
    @XmlJavaTypeAdapter(PermissionsAdapter.class)
    private Map<String,Permission> permissions = new TreeMap<>();

    @Generated(value = "generated by IDE")
    public Long getId() {
        return id;
    }

    @Generated(value = "generated by IDE")
    public void setId(Long id) {
        this.id = id;
    }

    @Generated(value = "generated by IDE")
    public Map<String, Permission> getPermissions() {
        return permissions;
    }

    @Generated(value = "generated by IDE")
    public void setPermissions(Map<String, Permission> permissions) {
        this.permissions = permissions;
    }

    @Generated(value = "generated by IDE")
    public RoleName getName() {
        return name;
    }

    @Generated(value = "generated by IDE")
    public void setName(RoleName name) {
        this.name = name;
    }
}
