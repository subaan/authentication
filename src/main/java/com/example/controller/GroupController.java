package com.example.controller;

import com.example.constants.GenericConstants;
import com.example.model.CurrentUser;
import com.example.model.Domain;
import com.example.model.Group;
import com.example.model.User;
import com.example.service.DomainService;
import com.example.service.GroupService;
import com.example.service.UserService;
import com.example.util.domain.vo.PagingAndSorting;
import com.example.util.web.ApiController;
import com.example.util.web.CRUDController;
import com.example.util.web.SortingUtil;
import com.example.vo.GroupVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;

/**
 * Created by Abdul on 30/5/16.
 */
@RestController
@RequestMapping("/api/group")
@Component
public class GroupController extends CRUDController<Group> implements ApiController {

    /** Logger constant. */
    private static final Logger LOGGER = LoggerFactory.getLogger(GroupController.class);

    /**
     * Auto wired domainService.
     */
    @Autowired
    private GroupService groupService;

    /**
     * Auto wired domainService.
     */
    @Autowired
    private DomainService domainService;

    /**
     * Auto wired userService.
     */
    @Autowired
    private UserService userService;

    /**
     * This method is used to return the group list.
     *
     * @return the group list.
     * @throws Exception default exception.
     */
    @RequestMapping(value = "", method = RequestMethod.GET)
    public Iterable<Group> list(@RequestParam(value = "domainId", required = false) Long domainId,
                                @RequestParam(value = "deleted", required = false) Boolean deleted) throws Exception {

        if(domainId != null) {
            Domain domain = domainService.find(domainId);
            return groupService.findAllByDomain(domain);
        } else if(deleted != null) {
            return groupService.findAllByDeleted(deleted);
        } else {
            return groupService.findAll();
        }
    }

    /**
     * This method is used to list the group by page range and sort by field.
     *
     * @param sortBy the sort field name
     *        example request param: sort(+name) - for Ascending order
     *                               sort(-name) - for Descending order
     * @param range the page range
     *        example request header: Range: items=0-9
     * @param request the http request object
     * @param response to http response object
     * @return the group list by page
     * @throws Exception the default exception.
     */
    @RequestMapping(value="/list", method = RequestMethod.GET)
    public List<Group> list(@RequestParam String sortBy, @RequestHeader(value = RANGE) String range,
                            @RequestParam(value = "domainId", required = false) Long domainId,
                            @RequestParam(value = "deleted", required = false) Boolean deleted,
                             HttpServletRequest request, HttpServletResponse response) throws Exception {
        //Set default values if null
        range = SortingUtil.defaultIfNullorEmpty(range, "0-10");
        sortBy = SortingUtil.defaultIfNullorEmpty(sortBy, "id");

        PagingAndSorting page = new PagingAndSorting(range, sortBy, Group.class);
        Page<Group> pageResponse = null;
        if(domainId != null) {
            Domain domain = domainService.find(domainId);
            pageResponse = groupService.findAllByDomain(page, domain);
        } else if(deleted != null) {
            pageResponse = groupService.findAllByDeleted(page, deleted);
        } else {
            pageResponse = groupService.findAll(page);
        }
        response.setHeader(GenericConstants.CONTENT_RANGE_HEADER, page.getPageHeaderValue(pageResponse));
        return pageResponse.getContent();
    }

    /**
     * This method is used to return the group by ID.
     *
     * @param id the group ID.
     * @return the domain.
     * @throws Exception default exception.
     */
    @Override
    @RequestMapping(value="/{id}", method = RequestMethod.GET)
    public Group read(@PathVariable(PATH_ID) Long id) throws Exception {
        return groupService.find(id);
    }

    /**
     * This method is used to create new group.
     *
     * @param group the group request object.
     * @return the success/ failure message.
     * @throws Exception default exception.
     */
    @Override
    public Group create(@RequestBody Group group) throws Exception {
        return groupService.create(group);
    }

    /**
     * This method is used to create new group.
     *
     * @param groupVO the groupVO object
     * @return the group.
     * @throws Exception  default exception.
     */
    @RequestMapping(value="/create", method = RequestMethod.POST,
            produces = { MediaType.APPLICATION_JSON_VALUE },
            consumes = { MediaType.APPLICATION_JSON_VALUE })
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public Group createGroup(@RequestBody GroupVO groupVO) throws Exception {

        //To check Group name already exist with the domain
        Domain domain = domainService.find(groupVO.getDomainId());
        Boolean isGroupExist = groupService.isGroupExist(groupVO.getName(), domain);

        if(!isGroupExist) {
            Group group = new Group();
            group.setName(groupVO.getName());
            group.setDomain(domainService.find(groupVO.getDomainId()));
            group.setUsers(userService.findAllByIds(groupVO.getUsers()));
            group.setCreatedDateTime(new Date());
            return groupService.create(group);
        } else {
            throw new Exception("Group already exist in the domain");
        }
    }

    /**
     * This method is used to update group.
     *
     * @param group the group request object.
     * @param id the group ID.
     * @return success/failure of the update.
     * @throws Exception default exception.
     */
    @Override
    public  Group update(@RequestBody Group group, @PathVariable(PATH_ID) Long id) throws  Exception {
        //TODO:  Need to add update logic. For testing this will be used
        return groupService.update(group);
    }

    /**
     * This method is used to update the existing group.
     *
     * @param id - Id of the group id
     * @param groupVO the groupVO object
     * @return the group.
     * @throws Exception  default exception.
     */
    @RequestMapping(value="/update/{id}", method = RequestMethod.PATCH,
            produces = { MediaType.APPLICATION_JSON_VALUE },
            consumes = { MediaType.APPLICATION_JSON_VALUE })
    @ResponseStatus(HttpStatus.ACCEPTED)
    @ResponseBody
    public Group updateGroup(@RequestBody GroupVO groupVO, @PathVariable(PATH_ID) Long id) throws Exception {

        //To check Group name already exist with the domain
        Domain domain = domainService.find(groupVO.getDomainId());
        Boolean isGroupExist = groupService.isGroupExist(groupVO.getName(), domain);

        if(!isGroupExist) {
            Group group = groupService.find(id);
            if(group != null) {
                group.setName(groupVO.getName());
                group.setUsers(userService.findAllByIds(groupVO.getUsers()));
                group.setLastModifiedDateTime(new Date());
            }
            return groupService.update(group);

        } else {
            throw new Exception("Group already exist in the domain");
        }

    }

    /**
     * This method is soft delete the group.
     *
     * @param id the group ID.
     * @return success/failure of the update.
     * @throws Exception default exception.
     */
    @RequestMapping(value="/delete/{id}", method = RequestMethod.DELETE,
            produces = { MediaType.APPLICATION_JSON_VALUE })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteGroup(@PathVariable(PATH_ID) Long id) throws  Exception {
        Group group = groupService.find(id);
        if(group != null) {
            group.setDeleted(true);
            group.setDeletedDateTime(new Date());
        }
        groupService.update(group);
    }

    /**
     * This method is used to delete the group.
     *
     * @param id the group ID.
     * @throws Exception
     */
    @Override
    public void delete(@PathVariable(PATH_ID) Long id) throws Exception {
        groupService.delete(id);
    }
}
