package com.example.repository;

import com.example.model.User;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Created by Abdul on 19/5/16.
 */
public interface UserRepository extends PagingAndSortingRepository<User, Long> {

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
