package com.example.service;

import com.example.model.User;
import org.springframework.stereotype.Service;

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
    Iterable<User> findAll();

    /**
     * This method find user with specified user name from active directory.
     *
     * @param username the username
     * @return User.
     */
    User findByUsername(String  username);
}
