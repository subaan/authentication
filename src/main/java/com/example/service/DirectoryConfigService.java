package com.example.service;

import com.example.model.DirectoryConfig;
import com.example.util.domain.CRUDService;
import org.springframework.stereotype.Service;

/**
 * Created by Abdul on 05/6/16.
 */
@Service
public interface DirectoryConfigService extends CRUDService<DirectoryConfig> {

    /**
     * This method find directory config by domain ID.
     *
     * @param domainId the domain ID
     * @return directory config.
     */
    DirectoryConfig findByDomainId(Long domainId);

}
