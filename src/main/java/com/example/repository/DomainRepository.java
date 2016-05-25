package com.example.repository;

import com.example.model.Domain;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Created by Abdul on 19/5/16.
 */
public interface DomainRepository extends PagingAndSortingRepository<Domain, Long> {

    /**
     * This method find domain with specified domain name.
     *
     * @param name the domain name
     * @return Domain.
     */
    Domain findByAliasName(String name);
}
