/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlTransient;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Audit columns, for report.
 *
 * @author Abdul
 */
public class AuditColumns {


     /** Created by user. */
    @CreatedBy
    @JoinColumn(name = "created_user_id", referencedColumnName = "id")
    @OneToOne
    private User createdBy;

    /** Last updated by user. */
    @LastModifiedBy
    @JoinColumn(name = "updated_user_id", referencedColumnName = "id")
    @OneToOne
    private User updatedBy;

    /** Deleted by user. */
    @LastModifiedBy
    @JoinColumn(name = "deleted_user_id", referencedColumnName = "id")
    @OneToOne
    private User deletedBy;

    /** Created date and time. */
    @CreatedDate
    @DateTimeFormat(style = "M-")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDateTime;

    /** Last modified date and time. */
    @LastModifiedDate
    @DateTimeFormat(style = "M-")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastModifiedDateTime;

    /** Deleted date and time. */
    @LastModifiedDate
    @DateTimeFormat(style = "M-")
    @Temporal(TemporalType.TIMESTAMP)
    private Date deletedDateTime;

    @Transient
    @JsonIgnore
    @XmlTransient
    SimpleDateFormat format = new SimpleDateFormat("EEE MMM d HH:mm:ss zzz yyyy", Locale.getDefault());


    /**
     * Get the createdBy.
     * @return createdBy
     */
    public User getCreatedBy() {
        return createdBy;
    }

    /**
     * Set the createdBy.
     * @param createdBy - the User to set
     */
    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    /**
     * Get the updatedBy.
     * @return updatedBy
     */
    public User getUpdatedBy() {
        return updatedBy;
    }

    /**
     * Set the updatedBy.
     * @param updatedBy - the User to set
     */
    public void setUpdatedBy(User updatedBy) {
        this.updatedBy = updatedBy;
    }

    /**
     * Get the createdDateTime.
     * @return createdDateTime
     */
    public Date getCreatedDateTime() {

        if(this.createdDateTime == null) {
            return createdDateTime;
        } else {
            try {
                return format.parse(createdDateTime.toString());
            } catch (Exception ex) {
                return createdDateTime;
            }
        }
    }

    /**
     * Set the createdDateTime.
     * @param createdDateTime - the DateTime to set
     */
    public void setCreatedDateTime(Date createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    /**
     * Get the lastModifiedDateTime.
     * @return lastModifiedDateTime
     */
    public Date getLastModifiedDateTime() {

        if(this.lastModifiedDateTime == null) {
            return lastModifiedDateTime;
        } else {
            try {
                return format.parse(lastModifiedDateTime.toString());
            } catch (Exception ex) {
                return lastModifiedDateTime;
            }
        }
    }

    /**
     * Set the lastModifiedDateTime.
     * @param lastModifiedDateTime - the DateTime to set
     */
    public void setLastModifiedDateTime(Date lastModifiedDateTime) {
        this.lastModifiedDateTime = lastModifiedDateTime;
    }

    /**
     * Get the deletedBy.
     * @return deletedBy
     */
    public User getDeletedBy() {
        return deletedBy;
    }

    /**
     * Set the deletedBy.
     * @param deletedBy - the User to set
     */
    public void setDeletedBy(User deletedBy) {
        this.deletedBy = deletedBy;
    }

    /**
     * Get the deletedDateTime.
     * @return deletedDateTime
     */
    public Date getDeletedDateTime() {

        if(this.deletedDateTime == null) {
            return deletedDateTime;
        } else {
            try {
                return format.parse(deletedDateTime.toString());
            } catch (Exception ex) {
                return deletedDateTime;
            }
        }
    }

    /**
     * Set the deletedDateTime.
     * @param deletedDateTime - the DateTime to set
     */
    public void setDeletedDateTime(Date deletedDateTime) {
        this.deletedDateTime = deletedDateTime;
    }

}
