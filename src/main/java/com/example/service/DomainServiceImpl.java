package com.example.service;

import com.example.model.Domain;
import com.example.model.User;
import com.example.repository.DomainRepository;
import com.example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Abdul on 19/5/16.
 */
@Service
public class DomainServiceImpl implements DomainService {

    @Autowired
    private DomainRepository domainRepository;

    @Autowired
    private UserRepository userRepository;

    /** Validator attribute. */
//    @Autowired
//    private AppValidator validator;

    @Override
    public void createDomain(Domain domain, User user) throws Exception {

        System.out.println(" Domain create ");

        domainRepository.save(domain);
        userRepository.save(user);

        System.out.println(" Domain successfully created ");

    }
}
