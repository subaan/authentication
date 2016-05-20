package com.example.controller;

import com.example.constants.GenericConstants;
import com.example.model.Domain;
import com.example.model.User;
import com.example.service.DomainService;
import com.example.util.domain.vo.PagingAndSorting;
import com.example.util.web.CRUDController;
import com.example.vo.DomainVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import org.w3c.dom.DOMImplementation;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;

/**
 * Created by Abdul on 19/5/16.
 */
@RestController
@RequestMapping("/domain")
@Component
public class DomainController extends CRUDController {

    /**
     * Auto wired domainService.
     */
    @Autowired
    private DomainService domainService;

    /**
     * This method is used to return the domain list.
     * @return the domain list.
     * @throws Exception default exception.
     */
    @RequestMapping(value = "", method = RequestMethod.GET)
    public Iterable<Domain> list() throws Exception {
        return domainService.findAll();
    }

    @Override
    @RequestMapping(value="/list", method = RequestMethod.GET)
    public List<Domain> list(@RequestParam String sortBy, @RequestHeader(value = "Range") String range,
                              HttpServletRequest request, HttpServletResponse response) throws Exception {

        PagingAndSorting page = new PagingAndSorting(range, sortBy, Domain.class);
        Page<Domain> pageResponse = domainService.findAll(page);
        response.setHeader(GenericConstants.CONTENT_RANGE_HEADER, page.getPageHeaderValue(pageResponse));
        return pageResponse.getContent();
    }

    /**
     * This method is used to return the domain by ID.
     * @param id the domain ID.
     * @return the domain list.
     * @throws Exception default exception.
     */
    @RequestMapping(value="/{id}", method = RequestMethod.GET)
    public Domain getDomain(@PathVariable("id") Long id) throws Exception {
        return domainService.find(id);
    }

    /**
     * This method is used to create new domain.
     * @param domain the domain request object.
     * @throws Exception default exception.
     */
    @RequestMapping(value = "", method = RequestMethod.POST,
            produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody Domain domain) throws Exception {
        domainService.create(domain);
    }

    /**
     * Add new domain.
     * @param  domainVO domainVO object
     * @return success/failure of create.
     * @throws Exception default exception.
     */
    @RequestMapping(method = RequestMethod.POST, value = "/create",
            produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseStatus(HttpStatus.OK)
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
        user.setUsername(domainVO.getUserName());
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
     * @param domainVO the domainVO request object.
     * @param id the domain ID.
     * @return success/failure of the update.
     * @throws Exception default exception.
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT,
            produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseStatus(HttpStatus.CREATED)
    public String update(@RequestBody DomainVO domainVO, @PathVariable("id") Long id) throws  Exception {
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
     * This method is used to update the domain status.
     * @param id the domain ID.
     * @return success/failure of the update.
     * @throws Exception default exception.
     */
    @RequestMapping(value = "/approve/{id}", method = RequestMethod.PUT,
            produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseStatus(HttpStatus.CREATED)
    public String approveDoamin(@PathVariable("id") Long id) throws  Exception {
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
     * @param id the domain ID.
     * @throws Exception
     */
    @RequestMapping(value="/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") Long id) throws Exception {
        domainService.delete(id);
    }


}
