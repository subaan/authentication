package com.example.repository;

import com.example.model.DirectoryConfig;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Created by Abdul on 10/6/16.
 */
public interface DirectoryConfigRepository extends PagingAndSortingRepository<DirectoryConfig, Long> {

    /**
     * This method find directory config by domain ID.
     *
     * @param domainId the domain ID
     * @return directory config.
     */
    DirectoryConfig findByDomainId(Long domainId);
}
