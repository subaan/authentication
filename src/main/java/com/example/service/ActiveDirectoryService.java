package com.example.service;

import com.example.model.User;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.stereotype.Service;

import javax.naming.InvalidNameException;
import java.io.UnsupportedEncodingException;

/**
 * Created by Abdul on 3/6/16.
 */
@Service
public interface ActiveDirectoryService {

    /**
     * This method is used to return the user list form AD.
     *
     * @return the user list
     */
    Iterable<User> findAll(Long domainId);

    /**
     * This method find user with specified user name from active directory.
     *
     * @param username the username
     * @param domainId the domain ID
     * @return User.
     */
    User findByUsername(String  username, Long domainId);

    /**
     * This method used to create new user to active directory.
     *
     * @param user the user object.
     * @param domainId the domain ID
     * @return User.
     */
    void create(User user, Long domainId) throws InvalidNameException, UnsupportedEncodingException;

    /**
     * This method is used to get the ldapTemplate.
     * @param domainId the domain id.
     * @return the ldapTemplate.
     */
    LdapTemplate ldapTemplate(Long domainId);

    void authenticate() throws  InvalidNameException;
}
