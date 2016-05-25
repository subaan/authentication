package com.example.controller;

import com.example.constants.GenericConstants;
import com.example.model.User;
import com.example.repository.DomainRepository;
import com.example.service.UserService;
import com.example.util.domain.vo.PagingAndSorting;
import com.example.util.web.ApiController;
import com.example.util.web.CRUDController;
import com.example.util.web.SortingUtil;
import com.example.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;

/**
 * Created by Abdul on 19/5/16.
 */
@RestController
@RequestMapping("/user")
public class UserController extends CRUDController<User> implements ApiController {

    /**
     * Auto wired userService.
     */
    @Autowired
    private UserService userService;

    /**
     * Auto wired domainRepository.
     */
    @Autowired
    private DomainRepository domainRepository;

    /**
     * This method is used to return the user list.
     *
     * @return the user list.
     * @throws Exception default exception.
     */
    @RequestMapping(value = "", method = RequestMethod.GET)
    public Iterable<User> list() throws  Exception {
        return userService.findAll();
    }

    /**
     * This method is used to list the user by page range and sort by field.
     *
     * @param sortBy the sort field name
     *        example request param: sort(+name) - for Ascending order
     *                               sort(-name) - for Descending order
     * @param range the page range
     *        example request header: Range: items=0-9
     * @param request the http request object
     * @param response to http response object
     * @return the domain list by page
     * @throws Exception the default exception.
     */
    @Override
    @RequestMapping(value="/list", method = RequestMethod.GET)
    public List<User> list(@RequestParam String sortBy, @RequestHeader(value = RANGE) String range,
                             HttpServletRequest request, HttpServletResponse response) throws Exception {
        //Set default value if null
        range = SortingUtil.defaultIfNullorEmpty(range, "0-10");
        sortBy = SortingUtil.defaultIfNullorEmpty(sortBy, "id");

        PagingAndSorting page = new PagingAndSorting(range, sortBy, User.class);
        Page<User> pageResponse = userService.findAll(page);
        response.setHeader(GenericConstants.CONTENT_RANGE_HEADER, page.getPageHeaderValue(pageResponse));
        return pageResponse.getContent();
    }

    /**
     * This method is used to return the user by ID.
     * @param id the user ID.
     * @return the user.
     * @throws Exception default exception.
     */
    @Override
    public User read(@PathVariable(PATH_ID) Long id) throws  Exception {
        return userService.find(id);
    }

    /**
     * This method is used to create new user.
     *
     * @param user the user request object.
     * @throws Exception default exception.
     */
    @RequestMapping(value = "", method = RequestMethod.POST,
            produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseStatus(HttpStatus.CREATED)
    public User create(@RequestBody User user) throws  Exception {
        return userService.create(user);
    }

    /**
     * This method is used to create new user by domain.
     *
     * @param userVO the userVO request object.
     * @return the success or failure message as JSON.
     * @throws Exception default exception.
     */
    @RequestMapping(value = "/create", method = RequestMethod.POST,
            produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseStatus(HttpStatus.CREATED)
    public String createUser(@RequestBody UserVO userVO) throws  Exception {

        //Create new user
        User user = new User();
        user.setUsername(userVO.getUsername());
        user.setPassword(userVO.getPassword());
        user.setEmailId(userVO.getEmailId());
        user.setDomain(domainRepository.findOne(userVO.getDomainId()));
        user.setStatus(User.UserStatus.valueOf(userVO.getStatus()));
        user.setType(User.UserType.valueOf(userVO.getType()));
        user.setCreatedDate(new Date());

        userService.create(user);

        return "{\"result\":\"success\"}";
    }

    /**
     * This method is used to update user.
     *
     * @param userVO the userVO request object.
     * @param id the user ID.
     * @return the success or failure message as JSON.
     * @throws Exception default exception.
     */
    @RequestMapping(value = "/update/{id}", method = RequestMethod.PUT,
            produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseStatus(HttpStatus.ACCEPTED)
    public String updateUser(@RequestBody UserVO userVO, @PathVariable(PATH_ID) Long id) throws Exception {
        //Get existing user
        User existinguser = userService.find(id);
        //Update new values
        existinguser.setUsername(userVO.getUsername());
        existinguser.setPassword(userVO.getPassword());
        existinguser.setEmailId(userVO.getEmailId());
        existinguser.setStatus(User.UserStatus.valueOf(userVO.getStatus()));
        existinguser.setType(User.UserType.valueOf(userVO.getType()));
        existinguser.setUpdatedDate(new Date());
        userService.update(existinguser);

        return "{\"result\":\"success\"}";
    }

    /**
     * This method is used to update the user.
     *
     * @param user the user request object.
     * @param id - Id of the entity to update
     * @return the user
     * @throws Exception the default exception.
     */
    @Override
    public User update(@RequestBody User user, @PathVariable(PATH_ID) Long id) throws  Exception {
        //TODO:  Need to add update logic. For testing this will be used
        return userService.update(user);
    }

    /**
     * This method is used to delete the user from DB.
     *
     * @param id the user ID.
     * @throws Exception
     */
    @Override
    public void delete(@PathVariable(PATH_ID) Long id) throws Exception {
        userService.delete(id);
    }

    /**
     * This method is used to delete the user. This is soft delete.
     *
     * @param id the user ID.
     * @throws Exception
     */
    @RequestMapping(value="/delete/{id}", method = RequestMethod.DELETE)
    public void deleteUser(@PathVariable(PATH_ID) Long id) throws Exception {

        //Get existing user
        User user = userService.find(id);
        //Soft delete the user
        user.setDeleted(true);
        userService.update(user);
    }

}
