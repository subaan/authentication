package com.example.repository;

import com.example.model.Domain;
import com.example.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

/**
 * Created by Abdul on 19/5/16.
 */
public interface UserRepository extends PagingAndSortingRepository<User, Long> {


    /**
     * This method is used to return the user list by domain.
     * @param domain the domain object.
     *
     * @return entity
     * @throws Exception if error occurs
     */
    Iterable<User> findAllByDomain(Domain domain);

    /**
     * This method is used to return the user list by domain.
     * @param pageable the pageable object.
     * @param domain the domain object.
     *
     * @return entity
     * @throws Exception if error occurs
     */
    Page<User> findAllByDomain(Pageable pageable, Domain domain);

    /**
     * This method find user with specified user name.
     *
     * @param username the username
     * @return User.
     */
    User findFirstByUsername(String  username);

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
    User findByUsernameAndPassword(String username, String password);

    /**
     * This method find user with specified user name,password and domain.
     *
     * @param username the username.
     * @param password the password.
     * @param domain the domain object.
     * @return User.
     */
//    @Query("SELECT u FROM User u WHERE u.username = :username and u.password = :password and u.domain = :domain")
    User findByUsernameAndPasswordAndDomain(String username, String password, Domain domain);
}
