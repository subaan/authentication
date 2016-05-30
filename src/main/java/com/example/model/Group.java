package com.example.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Abdul on 28/5/16.
 */
@Entity
@Table(name="domain_group")
public class Group extends AuditColumns {

    /** Auto generated ID */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /** The name of group */
    @NotNull
    @Size(min = 1, max = 20)
    private String name;

    /** The group users */
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "group_users",joinColumns = {@JoinColumn(name = "group_id")}, inverseJoinColumns = {@JoinColumn(name = "user_id")})
    private List<User> users = new ArrayList<User>();

    /** The unique email ID of user */
    @ManyToOne
    @JoinColumn(name = "domain_id")
    @NotNull
    private Domain domain;

    /** The boolean value to represent soft delete */
    private boolean deleted = false;

    /**
     * Get the group ID.
     * @return the group ID
     */
    public Long getId() {
        return id;
    }

    /**
     * Set the group ID.
     * @param id - the group ID.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Get the group name.
     * @return the group name
     */
    public String getName() {
        return name;
    }

    /**
     * Set the group name.
     * @param name - the group name.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get the domain.
     * @return the domain
     */
    public Domain getDomain() {
        return domain;
    }

    /**
     * Set the domain.
     * @param domain - the domain object.
     */
    public void setDomain(Domain domain) {
        this.domain = domain;
    }

    /**
     * Gets the users.
     *
     * @return Value of users.
     */
    public List<User> getUsers() {
        return users;
    }

    /**
     * Set the users.
     * @param users - the user list
     */
    public void setUsers(List<User> users) {
        this.users = users;
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
