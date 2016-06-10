package com.example.service;

import com.example.model.DirectoryConfig;
import com.example.model.Domain;
import com.example.model.Group;
import com.example.repository.DirectoryConfigRepository;
import com.example.repository.GroupRepository;
import com.example.util.domain.vo.PagingAndSorting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

/**
 * Created by Abdul on 19/5/16.
 */
@Service
public class DirectoryConfigServiceImpl implements DirectoryConfigService {

    @Autowired
    private DirectoryConfigRepository directoryConfigRepository;

    @Override
    public Iterable<DirectoryConfig> findAll() throws Exception {
        return directoryConfigRepository.findAll();
    }

    @Override
    public Page<DirectoryConfig> findAll(PagingAndSorting pagingAndSorting) throws Exception {
        return directoryConfigRepository.findAll(pagingAndSorting.toPageRequest());
    }

    @Override
    public DirectoryConfig find(Long id) throws Exception {
        return directoryConfigRepository.findOne(id);
    }

    @Override
    public DirectoryConfig create(DirectoryConfig directoryConfig) throws Exception {
        return directoryConfigRepository.save(directoryConfig);
    }

    @Override
    public DirectoryConfig update(DirectoryConfig directoryConfig) throws Exception {
        return directoryConfigRepository.save(directoryConfig);
    }

    @Override
    public void delete(DirectoryConfig directoryConfig) throws Exception {
        directoryConfigRepository.delete(directoryConfig);
    }

    @Override
    public void delete(Long id) throws Exception {
        directoryConfigRepository.delete(id);
    }

    @Override
    public DirectoryConfig findByDomainId(Long domainId) {
        return directoryConfigRepository.findByDomainId(domainId);
    }
}
