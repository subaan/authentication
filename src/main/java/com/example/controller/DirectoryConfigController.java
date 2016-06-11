package com.example.controller;

import com.example.constants.GenericConstants;
import com.example.model.DirectoryConfig;
import com.example.model.Domain;
import com.example.service.DirectoryConfigService;
import com.example.util.domain.vo.PagingAndSorting;
import com.example.util.web.ApiController;
import com.example.util.web.CRUDController;
import com.example.util.web.SortingUtil;
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
@RequestMapping("/api/directoryconfig")
@Component
public class DirectoryConfigController extends CRUDController<DirectoryConfig> implements ApiController {

    /** Logger constant. */
    private static final Logger LOGGER = LoggerFactory.getLogger(DirectoryConfigController.class);

    /**
     * Auto wired domainService.
     */
    @Autowired
    private DirectoryConfigService directoryConfigService;


    /**
     * This method is used to return the directory config list.
     *
     * @return the directory config.
     * @throws Exception default exception.
     */
    @RequestMapping(value = "", method = RequestMethod.GET)
    public Iterable<DirectoryConfig> list() throws Exception {

        return  directoryConfigService.findAll();
    }

    /**
     * This method is used to list the directory config by page range and sort by field.
     *
     * @param sortBy the sort field name
     *        example request param: sort(+name) - for Ascending order
     *                               sort(-name) - for Descending order
     * @param range the page range
     *        example request header: Range: items=0-9
     * @param request the http request object
     * @param response to http response object
     * @return the directory config list
     * @throws Exception the default exception.
     */
    @RequestMapping(value="/list", method = RequestMethod.GET)
    public List<DirectoryConfig> list(@RequestParam String sortBy, @RequestHeader(value = RANGE) String range,
                             HttpServletRequest request, HttpServletResponse response) throws Exception {
        //Set default values if null
        range = SortingUtil.defaultIfNullorEmpty(range, "0-10");
        sortBy = SortingUtil.defaultIfNullorEmpty(sortBy, "id");

        PagingAndSorting page = new PagingAndSorting(range, sortBy, DirectoryConfig.class);
        Page<DirectoryConfig> pageResponse = directoryConfigService.findAll(page);
        response.setHeader(GenericConstants.CONTENT_RANGE_HEADER, page.getPageHeaderValue(pageResponse));
        return pageResponse.getContent();
    }

    /**
     * This method is used to return the directory config by ID.
     *
     * @param id the directory config ID.
     * @return the directory config.
     * @throws Exception default exception.
     */
    @Override
    @RequestMapping(value="/{id}", method = RequestMethod.GET)
    public DirectoryConfig read(@PathVariable(PATH_ID) Long id) throws Exception {
        return directoryConfigService.find(id);
    }

    /**
     * This method is used to create new directory config.
     *
     * @param directoryConfig the directory config request object.
     * @return the success/ failure message.
     * @throws Exception default exception.
     */
    @Override
    public DirectoryConfig create(@RequestBody DirectoryConfig directoryConfig) throws Exception {
        return directoryConfigService.create(directoryConfig);
    }


    /**
     * This method is used to update directoryConfig.
     *
     * @param directoryConfig the directoryConfig request object.
     * @param id the directoryConfig ID.
     * @return success/failure of the update.
     * @throws Exception default exception.
     */
    @Override
    public  DirectoryConfig update(@RequestBody DirectoryConfig directoryConfig, @PathVariable(PATH_ID) Long id) throws  Exception {
        //TODO:  Need to add update logic. For testing this will be used
        return directoryConfigService.update(directoryConfig);
    }

    /**
     * This method is used to delete the directoryConfig.
     *
     * @param id the directoryConfig ID.
     * @throws Exception
     */
    @Override
    public void delete(@PathVariable(PATH_ID) Long id) throws Exception {
        directoryConfigService.delete(id);
    }
}
