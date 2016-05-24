package com.example.service;

import com.example.model.User;
import com.example.util.domain.CRUDService;

/**
 * Created by Abdul on 20/5/16.
 */
public interface UserService extends CRUDService<User> {

    /**
     * This method find user with specified user name.
     *
     * @param username the username
     * @return User.
     */
    User findByUsername(String  username);

    /**
     * This method find user with specified user name and password.
     *
     * @param username the username.
     * @param password the password.
     * @return User.
     */
    User findByUsernameAndPassword(String  username, String password);

}
