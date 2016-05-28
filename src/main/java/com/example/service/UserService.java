package com.example.service;

import com.example.model.Domain;
import com.example.model.User;
import com.example.util.domain.CRUDService;
import com.example.util.domain.vo.PagingAndSorting;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Created by Abdul on 20/5/16.
 */
public interface UserService extends CRUDService<User> {

    /**
     * This method is used to return the user list by domain.
     * @param domain the domain object.
     *
     * @return entity
     * @throws Exception if error occurs
     */
    Iterable<User> findAllByDomain(Domain domain) throws Exception;

    /**
     * This method is used to return the user list by domain.
     * @param pagingAndSorting
     * @param domain the domain object.
     *
     * @return entity
     * @throws Exception if error occurs
     */
    Page<User> findAllByDomain(PagingAndSorting pagingAndSorting, Domain domain);

    /**
     * This method find user with specified user name.
     *
     * @param username the username
     * @return User.
     */
    User findByUsername(String  username);

    /**
     * This method find user with specified user name.
     *
     * @param username the username.
     * @param domain the domain object.
     * @return User.
     */
    User findByUsernameAndDomain(String username, Domain domain);

    /**
     * This method find user with specified user name and password.
     *
     * @param username the username.
     * @param password the password.
     * @return User.
     */
    User findByUsernameAndPassword(String  username, String password);

    /**
     * This method find user with specified user name,password and domain.
     *
     * @param username the username.
     * @param password the password.
     * @param domain the domain object.
     * @return User.
     */
    User findByUsernameAndPasswordAndDomain(String username, String password, Domain domain);

    /**
     * This method is used to return boolean value if user already exist in the domain.
     *
     * @param username the user name
     * @param domain the domain.
     * @return the 'true' or false. If user already exist  in domain return true.
     */
    public Boolean isUserExist(String username, Domain domain);

}
