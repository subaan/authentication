package com.example.controller;

import com.example.model.User;
import com.example.repository.DomainRepository;
import com.example.repository.UserRepository;
import com.example.vo.UserVO;
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
     * Auto wired domainRepository.
     */
    @Autowired
    private DomainRepository domainRepository;

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
     * This method is used to create new user by domain.
     * @param userVO the userVO request object.
     * @return the success or failure message as JSON.
     */
    @RequestMapping(value = "/create", method = RequestMethod.POST,
            produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseStatus(HttpStatus.CREATED)
    public String createUser(@RequestBody UserVO userVO) {

        //Create new user
        User user = new User();
        user.setUsername(userVO.getUsername());
        user.setPassword(userVO.getPassword());
        user.setEmailId(userVO.getEmailId());
        user.setDomain(domainRepository.findOne(userVO.getDomainId()));
        user.setStatus(User.UserStatus.valueOf(userVO.getStatus()));
        user.setType(User.UserType.valueOf(userVO.getType()));
        user.setCreatedDate(new Date());

        userRepository.save(user);

        return "{\"result\":\"success\"}";
    }

    /**
     * This method is used to update user.
     * @param userVO the userVO request object.
     * @param id the user ID.
     * @return the success or failure message as JSON.
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT,
            produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseStatus(HttpStatus.CREATED)
    public String update(@RequestBody UserVO userVO, @PathVariable("id") Long id) {
        //Get existing user
        User existinguser = userRepository.findOne(id);
        //Update new values
        existinguser.setUsername(userVO.getUsername());
        existinguser.setPassword(userVO.getPassword());
        existinguser.setEmailId(userVO.getEmailId());
        existinguser.setStatus(User.UserStatus.valueOf(userVO.getStatus()));
        existinguser.setType(User.UserType.valueOf(userVO.getType()));
        existinguser.setUpdatedDate(new Date());
        userRepository.save(existinguser);

        return "{\"result\":\"success\"}";
    }

    /**
     * This method is used to delete the user from DB.
     * @param id the user ID.
     * @throws Exception
     */
    @RequestMapping(value="/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") Long id) throws Exception {
        userRepository.delete(id);
    }

    /**
     * This method is used to delete the user. This is soft delete.
     * @param id the user ID.
     * @throws Exception
     */
    @RequestMapping(value="/delete/{id}", method = RequestMethod.DELETE)
    public void deleteUser(@PathVariable("id") Long id) throws Exception {

        //Get existing user
        User user = userRepository.findOne(id);
        //Soft delete the user
        user.setDeleted(true);
        userRepository.save(user);
    }

}
