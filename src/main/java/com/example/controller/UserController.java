package com.example.controller;

import com.example.model.User;
import com.example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * Created by Abdul on 19/5/16.
 */
@RestController
@RequestMapping("/user")
public class UserController {

    /**
     * Auto wired UserRepository.
     */
    @Autowired
    private UserRepository userRepository;

    /**
     * This method is used to return the user list.
     * @return the user list.
     */
    @RequestMapping(value = "", method = RequestMethod.GET)
    public Iterable<User> list() {
        return userRepository.findAll();
    }

    /**
     * This method is used to return the user by ID.
     * @param id the user ID.
     * @return the user.
     */
    @RequestMapping(value="/{id}", method = RequestMethod.GET)
    public User getDomain(@PathVariable("id") Long id) {
        return userRepository.findOne(id);
    }

    /**
     * This method is used to create new user.
     * @param user the user request object.
     */
    @RequestMapping(value = "", method = RequestMethod.POST,
            produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody User user) {
        userRepository.save(user);
    }

    /**
     * This method is used to update user.
     * @param user the user request object.
     * @param id the user ID.
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT,
            produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseStatus(HttpStatus.CREATED)
    public String update(@RequestBody User user, @PathVariable("id") Long id) {
        //Get existing user
        User existinguser = userRepository.findOne(id);
        //Update new values
        existinguser.setUsername(user.getUsername());
        existinguser.setPassword(user.getPassword());
        existinguser.setEmailId(user.getEmailId());
        existinguser.setUpdatedDate(new Date());
        userRepository.save(existinguser);

        return "{\"result\":\"success\"}";
    }

    /**
     * This method is used to delete the user.
     * @param id the user ID.
     * @throws Exception
     */
    @RequestMapping(value="/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") Long id) throws Exception {
        userRepository.delete(id);
    }

}
