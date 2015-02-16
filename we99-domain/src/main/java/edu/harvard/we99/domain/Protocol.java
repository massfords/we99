package edu.harvard.we99.domain;

import javax.annotation.Generated;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * A protocol is a set of rules/procedures that govern the experiment. These
 * are defined externally from our system and are referenced here as a simple
 * label.
 *
 * @author mford
 */
@Entity
public class Protocol extends BaseEntity {
    /**
     * Primary key for this entity is generated
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Name of the protocol
     *
     * todo - make this @NotNull and Unique
     */
    private String name;

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
}
