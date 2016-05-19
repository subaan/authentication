package com.example.controller;

import com.example.model.Domain;
import com.example.model.User;
import com.example.repository.DomainRepository;
import com.example.service.DomainService;
import com.example.vo.DomainVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Date;

/**
 * Created by Abdul on 19/5/16.
 */
@RestController
@RequestMapping("/domain")
@Component
public class DomainController {

    /**
     * Auto wired domainRepository.
     */
    @Autowired
    private DomainRepository domainRepository;

    /**
     * Auto wired domainService.
     */
    @Autowired
    private DomainService domainService;

    /**
     * This method is used to return the domain list.
     * @return the domain list.
     */
    @RequestMapping(value = "", method = RequestMethod.GET)
    public Iterable<Domain> list() {
        return domainRepository.findAll();
    }

    /**
     * This method is used to return the domain by ID.
     * @param id the domain ID.
     * @return the domain list.
     */
    @RequestMapping(value="/{id}", method = RequestMethod.GET)
    public Domain getDomain(@PathVariable("id") Long id) {
        return domainRepository.findOne(id);
    }

    /**
     * This method is used to create new domain.
     * @param domain the domain request object.
     */
    @RequestMapping(value = "", method = RequestMethod.POST,
            produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody Domain domain) {
        domainRepository.save(domain);
    }

    /**
     * This method is used to update domain.
     * @param domain the domain request object.
     * @param id the user ID.
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT,
            produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseStatus(HttpStatus.CREATED)
    public String update(@RequestBody Domain domain, @PathVariable("id") Long id) {
        //Get existing domain
        Domain existingDomain = domainRepository.findOne(id);
        //Update new values
        existingDomain.setAliasName(domain.getAliasName());
        existingDomain.setOrganisationName(domain.getOrganisationName());
        existingDomain.setEmailId(domain.getEmailId());
        existingDomain.setBillingEmailId(domain.getBillingEmailId());
        existingDomain.setStreetAddress(domain.getStreetAddress());
        existingDomain.setCity(domain.getCity());
        existingDomain.setState(domain.getState());
        existingDomain.setCountry(domain.getCountry());
        existingDomain.setZipCode(domain.getZipCode());
        existingDomain.setPhoneNumber(domain.getPhoneNumber());
        existingDomain.setSignupDate(new Date());
        domainRepository.save(existingDomain);

        return "{\"result\":\"success\"}";
    }

    /**
     * This method is used to delete the domain.
     * @param id the domain ID.
     * @throws Exception
     */
    @RequestMapping(value="/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") Long id) throws Exception {
        domainRepository.delete(id);
    }

    /**
     * Add new domain.
     * @param  domainVO domainVO object
     * @return success/failure of the recovery.
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

}
