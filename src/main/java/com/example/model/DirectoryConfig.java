package com.example.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Created by Abdul on 10/6/16.
 */
@Entity
public class DirectoryConfig extends AuditColumns {

    /** Auto generated ID */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /** AD server url */
    @NotNull
    private String url;

    /** Base AD path - where in the LDAP tree should the search start. */
    @NotNull
    private String baseDN;

    /** Distinguished Name */
    @NotNull
    private String userDN;

    /** The admin password */
    @NotNull
    private String password;

    /** The domain id */
    @NotNull
    @Column(unique=true)
    private Long domainId;

    /**
     * Get the ID.
     * @return the ID
     */
    public Long getId() {
        return id;
    }

    /**
     * Set the ID.
     * @param id - the ID.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Get the admin password.
     * @return the admin password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Set the password.
     * @param password - the admin password.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Get the admin user distinguished name.
     * @return the admin password
     */
    public String getUserDN() {
        return userDN;
    }

    /**
     * Set distinguished name.
     * @param userDN - distinguished name.
     */
    public void setUserDN(String userDN) {
        this.userDN = userDN;
    }

    /**
     * Get the base distinguished name of AD.
     * @return the base distinguished name
     */
    public String getBaseDN() {
        return baseDN;
    }

    /**
     * Set the base distinguished name.
     * @param baseDN -  the base distinguished name.
     */
    public void setBaseDN(String baseDN) {
        this.baseDN = baseDN;
    }

    /**
     * Get the directory server url.
     * @return the directory server url
     */
    public String getUrl() {
        return url;
    }

    /**
     * Set the directory server url.
     * @param url -  the base distinguished name.
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * Get the domain id.
     * @return the domain id.
     */
    public Long getDomainId() {
        return domainId;
    }

    /**
     * Set the the domain id.
     * @param domainId -  the domain iD.
     */
    public void setDomain(Long domainId) {
        this.domainId = domainId;
    }
}
