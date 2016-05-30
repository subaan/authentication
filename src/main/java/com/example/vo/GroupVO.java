package com.example.vo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Abdul on 30/5/16.
 */
public class GroupVO {

    /** Auto generated ID */
    private Long id;

    /** The name of group */
    private String name;

    /** The user Id list */
    private List<Long> users = new ArrayList<Long>();

    /** The domain ID */
    private Long domainId;

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
     * Get the user IDs.
     * @return the user IDs
     */
    public List<Long> getUsers() {
        return users;
    }

    /**
     * Set the user ids.
     * @param users - the user ids.
     */
    public void setUsers(List<Long> users) {
        this.users = users;
    }

    /**
     * Get the domain id.
     * @return the domain id
     */
    public Long getDomainId() {
        return domainId;
    }

    /**
     * Set the domain ID.
     * @param domainId - the domain ID.
     */
    public void setDomainId(Long domainId) {
        this.domainId = domainId;
    }
}
