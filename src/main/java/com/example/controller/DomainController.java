package com.example.controller;

import com.example.constants.GenericConstants;
import com.example.model.CurrentlyLoggedUser;
import com.example.model.Domain;
import com.example.model.User;
import com.example.service.DomainService;
import com.example.util.domain.vo.PagingAndSorting;
import com.example.util.web.ApiController;
import com.example.util.web.CRUDController;
import com.example.util.web.SortingUtil;
import com.example.vo.DomainVO;
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
 * Created by Abdul on 19/5/16.
 */
@RestController
@RequestMapping("/api/domain")
@Component
public class DomainController extends CRUDController<Domain> implements ApiController {

    /** Logger constant. */
    private static final Logger LOGGER = LoggerFactory.getLogger(DomainController.class);

    /**
     * Auto wired domainService.
     */
    @Autowired
    private DomainService domainService;

    /**
     * This method is used to return the domain list.
     *
     * @return the domain list.
     * @throws Exception default exception.
     */
    @RequestMapping(value = "", method = RequestMethod.GET)
    public Iterable<Domain> list() throws Exception {
        return domainService.findAll();
    }

    /**
     * This method is used to list the domain by page range and sort by field.
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
    public List<Domain> list(@RequestParam String sortBy, @RequestHeader(value = RANGE) String range,
                              HttpServletRequest request, HttpServletResponse response) throws Exception {
        //Set default values if null
        range = SortingUtil.defaultIfNullorEmpty(range, "0-10");
        sortBy = SortingUtil.defaultIfNullorEmpty(sortBy, "id");

        PagingAndSorting page = new PagingAndSorting(range, sortBy, Domain.class);
        Page<Domain> pageResponse = domainService.findAll(page);
        response.setHeader(GenericConstants.CONTENT_RANGE_HEADER, page.getPageHeaderValue(pageResponse));
        return pageResponse.getContent();
    }

    /**
     * This method is used to return the domain by alias name.
     *
     * @return the domain.
     */
    @RequestMapping(value="/detail", method = RequestMethod.GET)
    public Domain findByAliasName(@RequestParam String aliasName, @CurrentlyLoggedUser User domainUser) {
        LOGGER.info("User Name: {} ", domainUser.getUsername());
        LOGGER.info("Domain Name: {} ", domainUser.getDomain().getAliasName());
        return domainService.findByAliasName(aliasName);
    }

    /**
     * This method is used to return the domain by ID.
     *
     * @param id the domain ID.
     * @return the domain list.
     * @throws Exception default exception.
     */
    @Override
    @RequestMapping(value="/{id}", method = RequestMethod.GET)
    public Domain read(@PathVariable(PATH_ID) Long id) throws Exception {
        return domainService.find(id);
    }

    /**
     * This method is used to create new domain.
     *
     * @param domain the domain request object.
     * @return the success/ failure message.
     * @throws Exception default exception.
     */
    @Override
    public Domain create(@RequestBody Domain domain) throws Exception {
        return domainService.create(domain);
    }

    /**
     * Add new domain.
     *
     * @param  domainVO domainVO object
     * @return success/failure of create.
     * @throws Exception default exception.
     */
    @RequestMapping(method = RequestMethod.POST, value = "/create",
            produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public String createDomain(@RequestBody DomainVO domainVO) throws Exception {

        //Set domain values
        Domain domain = new Domain();
        domain.setAliasName(domainVO.getAliasName());
        domain.setOrganisationName(domainVO.getOrganisationName());
        domain.setEmailId(domainVO.getEmailId());
        domain.setBillingEmailId(domainVO.getBillingEmailId());
        domain.setStreetAddress(domainVO.getStreetAddress());
        domain.setCity(domainVO.getCity());
        domain.setState(domainVO.getState());
        domain.setCountry(domainVO.getCountry());
        domain.setZipCode(domainVO.getZipCode());
        domain.setPhoneNumber(domainVO.getPhoneNumber());
        domain.setStatus(Domain.DomainStatus.APPROVAL_PENDING);
        domain.setSignupDate(new Date());

        //Set user values
        User user = new User();
        user.setUsername(domainVO.getUsername());
        user.setPassword(domainVO.getPassword());
        user.setEmailId(domainVO.getEmailId());
        user.setDomain(domain);
        user.setStatus(User.UserStatus.ENABLED);
        user.setType(User.UserType.DOMAIN_ADMIN);
        user.setCreatedDate(new Date());

        domainService.createDomain(domain, user);

        return "{\"result\":\"success\"}";
    }

    /**
     * This method is used to update domain.
     *
     * @param domainVO the domainVO request object.
     * @param id the domain ID.
     * @return success/failure of the update.
     * @throws Exception default exception.
     */
    @RequestMapping(value = "/update/{id}", method = RequestMethod.PUT,
            produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseStatus(HttpStatus.ACCEPTED)
    public String updateDomain(@RequestBody DomainVO domainVO, @PathVariable(PATH_ID) Long id) throws  Exception {
        //Get existing domain
        Domain existingDomain = domainService.find(id);
        //Update new values
        existingDomain.setAliasName(domainVO.getAliasName());
        existingDomain.setOrganisationName(domainVO.getOrganisationName());
        existingDomain.setEmailId(domainVO.getEmailId());
        existingDomain.setBillingEmailId(domainVO.getBillingEmailId());
        existingDomain.setStreetAddress(domainVO.getStreetAddress());
        existingDomain.setCity(domainVO.getCity());
        existingDomain.setState(domainVO.getState());
        existingDomain.setCountry(domainVO.getCountry());
        existingDomain.setZipCode(domainVO.getZipCode());
        existingDomain.setPhoneNumber(domainVO.getPhoneNumber());
        existingDomain.setStatus(Domain.DomainStatus.valueOf(domainVO.getStatus()));
        existingDomain.setUpdatedDate(new Date());
        domainService.update(existingDomain);

        return "{\"result\":\"success\"}";
    }

    /**
     * This method is used to update domain.
     *
     * @param domain the domain request object.
     * @param id the domain ID.
     * @return success/failure of the update.
     * @throws Exception default exception.
     */
     @Override
     public  Domain update(@RequestBody Domain domain, @PathVariable(PATH_ID) Long id) throws  Exception {
         //TODO:  Need to add update logic. For testing this will be used
         return domainService.update(domain);
     }

    /**
     * This method is used to update the domain status.
     *
     * @param id the domain ID.
     * @return success/failure of the update.
     * @throws Exception default exception.
     */
    @RequestMapping(value = "/approve/{id}", method = RequestMethod.PUT,
            produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseStatus(HttpStatus.CREATED)
    public String approve(@PathVariable(PATH_ID) Long id) throws  Exception {
        //Get existing domain
        Domain existingDomain = domainService.find(id);
        //Update status to 'APPROVAL_PENDING' to 'ACTIVE'
        existingDomain.setStatus(Domain.DomainStatus.ACTIVE);
        existingDomain.setUpdatedDate(new Date());
        domainService.update(existingDomain);

        return "{\"result\":\"success\"}";
    }

    /**
     * This method is used to delete the domain.
     *
     * @param id the domain ID.
     * @throws Exception
     */
    @Override
    public void delete(@PathVariable(PATH_ID) Long id) throws Exception {
        domainService.delete(id);
    }


}
