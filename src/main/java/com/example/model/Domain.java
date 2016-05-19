package com.example.model;


import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.persistence.Temporal;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlTransient;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * Created by Abdul on 18/5/16.
 */
@Entity
public class Domain {

    /** Auto generated ID */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /** The alise name of domain */
    @NotNull
    @Size(min = 1, max = 20)
    @Column(unique=true)
    private String aliasName;

    /** The organisation name of domain */
    @NotNull
    @Size(min = 1, max = 50)
    @Column(unique=true)
    private String organisationName;

    /** The unique email ID of domain */
    @NotNull
    @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\."
            +"[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@"
            +"(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?",
            message="{invalid.email}")
    @Column(unique=true)
    private String emailId;

    /** The email ID of the billing */
    @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\."
            +"[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@"
            +"(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?",
            message="{invalid.email}")
    private String billingEmailId;

    /** The street address */
    private String streetAddress;

    /** The city */
    private String city;

    /** The state detail */
    private String state;

    /** The country detail */
    private String country;

    /** The zip code city */
    private String zipCode;

    /** The phone number */
    @Pattern(regexp="(^$|[0-9]{10})",
            message="{invalid.phonenumber}")
    private String phoneNumber;

    /** The status of the domain */
    @Enumerated(EnumType.STRING)
    private DomainStatus status;

    /**
     *  The DomainStatus in enum values.
     */
    public enum DomainStatus {

        /** Once signup done, the domain status changed to APPROVAL_PENDING */
        APPROVAL_PENDING,
        /** Once New Domain has approved by super admin status changed to ACTIVE. This will be the active domain.*/
        ACTIVE,
        /** The domain suspended state. this will be suspended by super admin*/
        SUSPENDED,
        /** The domain closed state. No longer used*/
        CLOSED
    }

    /** The domain created date*/
    @CreatedDate
    @DateTimeFormat(style = "M-")
    @Temporal(TemporalType.TIMESTAMP)
    private Date signupDate;

    /** The domain last modified date */
    @LastModifiedDate
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedDate;

    /** The domain approved date*/
    @Temporal(TemporalType.TIMESTAMP)
    private Date approvedDate;

    @Transient
    @JsonIgnore
    @XmlTransient
    SimpleDateFormat format = new SimpleDateFormat("EEE MMM d HH:mm:ss zzz yyyy", Locale.getDefault());

    /**
     * Get the domain ID.
     * @return the domain ID
     */
    public Long getId() {
        return id;
    }

    /**
     * Set the domain ID.
     * @param id - the domain ID.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Get the user aliasName.
     * @return the aliasName
     */
    public String getAliasName() {
        return aliasName;
    }

    /**
     * Set the unique alias name of domain.
     * @param aliasName - the domain alias name
     */
    public void setAliasName(String aliasName) {
        this.aliasName = aliasName;
    }

    /**
     * Get the user organisationName.
     * @return the organisationName
     */
    public String getOrganisationName() {
        return organisationName;
    }

    /**
     * Set the organisation name of domain.
     * @param organisationName - the organisation name
     */
    public void setOrganisationName(String organisationName) {
        this.organisationName = organisationName;
    }

    /**
     * Get the user email ID of domain.
     * @return the email ID
     */
    public String getEmailId() {
        return emailId;
    }

    /**
     * Set the email ID.
     * @param emailId - the email ID
     */
    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    /**
     * Get the organisation email ID of domain.
     * @return the organisation email ID
     */
    public String getBillingEmailId() {
        return billingEmailId;
    }

    /**
     * Set the organisation email ID.
     * @param billingEmailId - the organisation email ID
     */
    public void setBillingEmailId(String billingEmailId) {
        this.billingEmailId = billingEmailId;
    }

    /**
     * Get the street address.
     * @return the street
     */
    public String getStreetAddress() {
        return streetAddress;
    }

    /**
     * Set the organisation street address.
     * @param streetAddress - the street address
     */
    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    /**
     * Get the city.
     * @return the city
     */
    public String getCity() {
        return city;
    }

    /**
     * Set the organisation city.
     * @param city - the city
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * Get the organisation state.
     * @return the organisation state
     */
    public String getState() {
        return state;
    }

    /**
     * Set the organisation state information.
     * @param state - the organisation state information
     */
    public void setState(String state) {
        this.state = state;
    }

    /**
     * Get the organisation country.
     * @return the organisation country
     */
    public String getCountry() {
        return country;
    }

    /**
     * Set the organisation country information.
     * @param country - the organisation country information
     */
    public void setCountry(String country) {
        this.country = country;
    }

    /**
     * Get the zip code.
     * @return the organisation country
     */
    public String getZipCode() {
        return zipCode;
    }

    /**
     * Set the organisation zip code.
     * @param zipCode - the organisation zip code
     */
    public void setZipCode(String zipCode) { this.zipCode = zipCode; }

    /**
     * Get the phone number.
     * @return the phone number
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Set the organisation phone number.
     * @param phoneNumber - the organisation phone number.
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * Get the domain status.
     * @return the domain status
     */
    public DomainStatus getStatus() {
        return status;
    }

    /**
     * Set the domain status.
     * @param status - the domain status.
     */
    public void setStatus(DomainStatus status) {
        this.status = status;
    }

    /**
     * Get the signup date.
     * @return the user signed date.
     */
    public Date getSignupDate() {
        if(this.signupDate == null) {
            try {
                return format.parse(new Date().toString());
            } catch (Exception ex) {
                return new Date();
            }
        } else {
            try {
                return format.parse(signupDate.toString());
            } catch (Exception ex) {
                return signupDate;
            }
        }
    }

    /**
     * Set the domain signed date.
     * @param signupDate - user signed date.
     */
    public void setSignupDate(Date signupDate) {
        this.signupDate = signupDate;
    }

    /**
     * Get the updated date.
     * @return the user updated date.
     */
    public Date getUpdatedDate() {
        return updatedDate;
    }

    /**
     * Set the user updated date.
     * @param updatedDate - user updated date.
     */
    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    /**
     * Get the domain approved date.
     * @return the domain approved date
     */
    public Date getApprovedDate() {
        return approvedDate;
    }

    /**
     * Set the domain approved date.
     * @param approvedDate - domain approved date.
     */
    public void setApprovedDate(Date approvedDate) {
        this.approvedDate = approvedDate;
    }


}
