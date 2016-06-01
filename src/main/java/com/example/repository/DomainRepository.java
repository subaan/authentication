package com.example.repository;

import com.example.model.Domain;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Created by Abdul on 19/5/16.
 */
public interface DomainRepository extends PagingAndSortingRepository<Domain, Long> {

    /**
     * This method get all domain with specified by deleted value.
     *
     * @param deleted is domain is soft deleted.
     * @return the group list.
     */
    Iterable<Domain> findAllByDeleted(boolean deleted);

    /**
     * This method is domain to return the user list by deleted value.
     *
     * @param pageable the pageable object.
     * @param deleted is domain is soft deleted.
     *
     * @return entity
     * @throws Exception if error occurs
     */
    Page<Domain> findAllByDeleted(Pageable pageable, boolean deleted);

    /**
     * This method find domain with specified domain name.
     *
     * @param name the domain name
     * @return Domain.
     */
    Domain findByAliasName(String name);

}
