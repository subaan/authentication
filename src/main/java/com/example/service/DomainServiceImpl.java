package com.example.service;

import com.example.model.Domain;
import com.example.model.User;
import com.example.repository.DomainRepository;
import com.example.repository.UserRepository;
import com.example.util.domain.vo.PagingAndSorting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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

    @Override
    public Iterable<Domain> findAll() throws Exception {
        return domainRepository.findAll();
    }

    @Override
    public Page<Domain> findAll(PagingAndSorting pagingAndSorting) throws Exception {
        return domainRepository.findAll(pagingAndSorting.toPageRequest());
    }

    @Override
    public Domain find(Long id) throws Exception {
        return domainRepository.findOne(id);
    }

    @Override
    public Domain create(Domain domain) throws Exception {
        return domainRepository.save(domain);
    }

    @Override
    public Domain update(Domain domain) throws Exception {
        return domainRepository.save(domain);
    }

    @Override
    public void delete(Domain domain) throws Exception {

    }

    @Override
    public void delete(Long id) throws Exception {
        domainRepository.delete(id);
    }

    @Override
    public void createDomain(Domain domain, User user) throws Exception {

        System.out.println(" Domain create ");

        domainRepository.save(domain);
        userRepository.save(user);

        System.out.println(" Domain successfully created ");

    }
}
