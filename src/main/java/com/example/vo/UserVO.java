package com.example.vo;

import java.util.Date;

/**
 * Created by Abdul on 19/5/16.
 */
public class UserVO {


    /** The name of user */
    private String username;

    /** The password of user */
    private String password;

    /** The unique email ID of user */
    private String emailId;

    /** The unique email ID of user */
    private Long domainId;

    /** The status of the user */
    private String status;

    /** The type of the user */
    private String type;

    /** The boolean value to represent soft delete */
    private boolean deleted;

    /** The date of user created*/
    private Date createdDate;

    /** The date of user updated*/
    private Date updatedDate;

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
     * Get the domain id of user.
     * @return the user domain id.
     */
    public Long getDomainId() {
        return domainId;
    }

    /**
     * Set the domain id.
     * @param domainId - the domain id.
     */
    public void setDomain(Long domainId) {
        this.domainId = domainId;
    }

    /**
     * Get the user status.
     * @return the user status.
     */
    public String getStatus() {
        return status;
    }

    /**
     * Set the status of the user.
     * @param status - the User status.
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Get the type of user.
     * @return the user type.
     */
    public String getType() {
        return type;
    }

    /**
     * Set the type of the user.
     * @param type - the User type.
     */
    public void setType(String type) {
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

    /**
     * Get the user create date.
     * @return the created date.
     */
    public Date getCreatedDate() {
        return createdDate;
    }

    /**
     * Set the created date of the user.
     * @param createdDate - the new User created date.
     */
    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    /**
     * Get the user updated date.
     * @return the updated date.
     */
    public Date getUpdatedDate() {
        return updatedDate;
    }

    /**
     * Set the last modified date of the user.
     * @param updatedDate - the User last modified date.
     */
    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }
}
