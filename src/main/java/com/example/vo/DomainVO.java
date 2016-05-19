package com.example.vo;

import java.util.Date;

/**
 * Created by Abdul on 19/5/16.
 */
public class DomainVO {

    /** Auto generated ID */
    private Long id;

    /** The alias name of domain */
    private String aliasName;

    /** The user name  */
    private String userName;

    /** The user password  */
    private String password;

    /** The organisation name of domain */
    private String organisationName;

    /** The unique email ID of domain */
    private String emailId;

    /** The email ID of the billing */
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
    private String phoneNumber;

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
     * Get the name of user.
     * @return the user name.
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Set the name.
     * @param userName - the User name
     */
    public void setUserName(String userName) { this.userName = userName; }

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
    public void setPassword(String password) { this.password = password; }


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

}
