package com.example.service;

import com.example.model.Domain;
import com.example.model.User;
import com.example.util.domain.CRUDService;
import com.example.util.domain.vo.PagingAndSorting;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

/**
 * Created by Abdul on 19/5/16.
 */
@Service
public interface DomainService extends CRUDService<Domain> {

    /**
     * This method get all user with specified by deleted value.
     *
     * @param deleted is the User is soft deleted.
     * @return the user list.
     */
    Iterable<Domain> findAllByDeleted(boolean deleted);

    /**
     * This method is User to return the user list by deleted value.
     *
     * @param pagingAndSorting
     * @param deleted is the user is soft deleted.
     *
     * @return entity
     * @throws Exception if error occurs
     */
    Page<Domain> findAllByDeleted(PagingAndSorting pagingAndSorting, boolean deleted);

    /**
     * This method find domain with specified domain name.
     *
     * @param name the domain name
     * @return Domain.
     */
    Domain findByAliasName(String name);

    /**
     * This method creates new domain.
     *
     * @param domain the domain object
     * @param user the user object
     * @throws Exception default exception.
     */
    void  createDomain(Domain domain, User user) throws Exception;
}
