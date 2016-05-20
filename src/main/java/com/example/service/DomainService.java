package com.example.service;

import com.example.model.Domain;
import com.example.model.User;
import com.example.util.domain.CRUDService;
import org.springframework.stereotype.Service;

/**
 * Created by Abdul on 19/5/16.
 */
@Service
public interface DomainService extends CRUDService<Domain> {

    /**
     * This method creates new domain.
     *
     * @param domain the domain object
     * @param user the user object
     * @throws Exception default exception.
     */
    void  createDomain(Domain domain, User user) throws Exception;
}
