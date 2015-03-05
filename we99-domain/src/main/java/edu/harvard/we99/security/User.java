package edu.harvard.we99.security;

import edu.harvard.we99.domain.BaseEntity;
import org.hibernate.validator.constraints.Email;

import javax.annotation.Generated;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlTransient;
import java.util.UUID;

/**
 * Models a user in the system. We're not currently using Roles but if we do then
 * we can add them as needed.
 *
 * @author mford
 */
@Entity
@Table(uniqueConstraints = {@UniqueConstraint( columnNames = {"email"})})
public class User extends BaseEntity {

    public enum PasswordStatus {assigned, temporary}

    /**
     * Primary key is generated
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * We'll use the user's email for communication and their login
     */
    @NotNull @Email
    private String email;

    /**
     * User's first name
     */
    @NotNull
    private String firstName;

    /**
     * User's last name
     */
    @NotNull
    private String lastName;

    /**
     * User's password. The db will store a SHA-256 salted and hashed password.
     * There's no point in exposing this value to the end user so it's marked
     * as XmlTransient so it doesn't appear in the UI
     */
    @NotNull @XmlTransient
    private String password;

    /**
     * Unique salt value for each user. This is stored along with their record
     * and is generated at the time of their creation.
     */
    @NotNull @XmlTransient
    private String salt;

    @ManyToOne(fetch = FetchType.EAGER)
    @NotNull
    private Role role;

    @NotNull
    @XmlTransient
    private PasswordStatus passwordStatus = PasswordStatus.temporary;

    @SuppressWarnings("UnusedDeclaration")
    public User() {}

    public User(String email, String firstName, String lastName) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = UUID.randomUUID().toString();
    }

    @PrePersist
    private void beforeInsert() {
        salt = UUID.randomUUID().toString();
    }

    @Generated(value = "generated by IDE")
    public String getEmail() {
        return email;
    }

    @Generated(value = "generated by IDE")
    public void setEmail(String email) {
        this.email = email;
    }

    @Generated(value = "generated by IDE")
    public String getFirstName() {
        return firstName;
    }

    @Generated(value = "generated by IDE")
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Generated(value = "generated by IDE")
    public Long getId() {
        return id;
    }

    @Override
    @Generated(value = "generated by IDE")
    public void setId(Long id) {
        this.id = id;
    }

    @Generated(value = "generated by IDE")
    public String getLastName() {
        return lastName;
    }

    @Generated(value = "generated by IDE")
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Generated(value = "generated by IDE")
    public String getPassword() {
        return password;
    }

    @Generated(value = "generated by IDE")
    public void setPassword(String password) {
        this.password = password;
    }

    @Generated(value = "generated by IDE")
    public String getSalt() {
        return salt;
    }

    @Generated(value = "generated by IDE")
    public Role getRole() {
        return role;
    }

    @Generated(value = "generated by IDE")
    public void setRole(Role role) {
        this.role = role;
    }

    @Generated(value = "generated by IDE")
    public PasswordStatus getPasswordStatus() {
        return passwordStatus;
    }

    @Generated(value = "generated by IDE")
    public void setPasswordStatus(PasswordStatus passwordStatus) {
        this.passwordStatus = passwordStatus;
    }
}
