package edu.harvard.we99.domain;

import javax.annotation.Generated;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.sql.Timestamp;
import java.util.Set;

/**
 * An experiment consists of one or more plates, a protocol, and some metadata
 * provided by the user like name, desc, etc.
 *
 * @author mford
 */
@Entity
public class Experiment extends BaseEntity {
    /**
     * The primary key for this entity is generated
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * User provided name for the experiment
     */
    private String name;

    /**
     * Timestamp for the creation of the experiment
     *
     * todo - look into using the new Java 8 datetime if there's a JPA/Hibernate annotation for it
     */
    private Timestamp created;

    /**
     * Reference to the protocol that governs this experiment
     */
    @ManyToOne(fetch = FetchType.EAGER)
    private Protocol protocol;


    /**
     * The plates that were used in the experiment
     *
     * todo - switch from a Set to a Map and store by the plate name
     */
    @OneToMany(fetch = FetchType.EAGER)
    private Set<Plate> plates;

    @Generated(value = "generated by IDE")
    public Timestamp getCreated() {
        return created;
    }

    @Generated(value = "generated by IDE")
    public void setCreated(Timestamp created) {
        this.created = created;
    }

    @Generated(value = "generated by IDE")
    public Long getId() {
        return id;
    }

    @Generated(value = "generated by IDE")
    public void setId(Long id) {
        this.id = id;
    }

    @Generated(value = "generated by IDE")
    public String getName() {
        return name;
    }

    @Generated(value = "generated by IDE")
    public void setName(String name) {
        this.name = name;
    }

    @Generated(value = "generated by IDE")
    public Protocol getProtocol() {
        return protocol;
    }

    @Generated(value = "generated by IDE")
    public void setProtocol(Protocol protocol) {
        this.protocol = protocol;
    }

    @Generated(value = "generated by IDE")
    public Set<Plate> getPlates() {
        return plates;
    }

    @Generated(value = "generated by IDE")
    public void setPlates(Set<Plate> plates) {
        this.plates = plates;
    }

    @Override
    @Generated(value = "generated by IDE")
    public String toString() {
        return "Experiment{" +
                "created=" + created +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", protocol=" + protocol +
                ", plates=" + plates +
                '}';
    }
}
