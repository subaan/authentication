package com.example.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Date;

/**
 * The user details has been manged.
 *
 * Created by Abdul on 18/5/16.
 */
@Entity
public class User extends AuditColumns {

    /** Auto generated ID */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /** The name of user */
    @NotNull
    @Size(min = 1, max = 20)
    private String username;

    /** The password of user */
    @JsonIgnore
    private String password;

    /** The unique email ID of user */
    @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\."
            +"[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@"
            +"(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?",
            message="{invalid.email}")
    private String emailId;

    /** The unique email ID of user */
    @ManyToOne
    @JoinColumn(name = "domain_id")
    @NotNull
    private Domain domain;

    /** The status of the user */
    @Enumerated(EnumType.STRING)
    private UserStatus status;

    /**
     *  The UserStatus in enum values.
     */
    public enum UserStatus {
        /** The user active state*/
        ENABLED,
        /** The user in active state */
        DISABLED
    }

    /** The type of the user */
    @Enumerated(EnumType.STRING)
    private UserType type;

    /**
     *  The UserType in enum values.
     */
    public enum UserType {
        /** The super admin user of application */
        SUPER_ADMIN,
        /** The root domain admin user */
        DOMAIN_ADMIN,
        /** The domain admin user */
        DOMAIN_USER
    }

    /** The boolean value to represent soft delete */
    @JsonIgnore
    private boolean deleted = false;

    /** The date of user created*/
    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;

    /** The date of user updated*/
    @LastModifiedDate
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedDate;
    
    /**
     * Parameterized constructor.
     */
    public User() {
       super();
    }
    
    /**
     * Parameterized constructor.
     * @param username to set
     */
    public User(String username, Domain domain) {
        this.username = username;
        this.domain = domain;
    }

    /**
     * Get the user ID.
     * @return the user ID
     */
    public Long getId() {
        return id;
    }

    /**
     * Set the user ID.
     * @param id - the User ID.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Get the name of user.
     * @return the user name.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Set the name.
     * @param username - the User name
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Get the user password.
     * @return the user password.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Set the password of user.
     * @param password - the User password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Get the unique email ID of the user.
     * @return the user email ID.
     */
    public String getEmailId() {
        return emailId;
    }

    /**
     * Set the email ID of the user.
     * @param emailId - the User email ID.
     */
    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    /**
     * Get the domain of user.
     * @return the user domain.
     */
    public Domain getDomain() {
        return domain;
    }

    /**
     * Set the domain.
     * @param domain - the domain.
     */
    public void setDomain(Domain domain) {
        this.domain = domain;
    }

    /**
     * Get the user status.
     * @return the user status.
     */
    public UserStatus getStatus() {
        return status;
    }

    /**
     * Set the status of the user.
     * @param status - the User status.
     */
    public void setStatus(UserStatus status) {
        this.status = status;
    }

    /**
     * Get the type of user.
     * @return the user type.
     */
    public UserType getType() {
        return type;
    }

    /**
     * Set the type of the user.
     * @param type - the User type.
     */
    public void setType(UserType type) {
        this.type = type;
    }

    /**
     * Get the boolean value isDeleted.
     * @return the boolean value.
     */
    public boolean isDeleted() {
        return deleted;
    }

    /**
     * Set the new value of deleted.
     * @param deleted - the deleted.
     */
    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

}
