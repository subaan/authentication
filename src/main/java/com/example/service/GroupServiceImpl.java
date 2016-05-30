package com.example.service;

import com.example.model.Domain;
import com.example.model.Group;
import com.example.repository.GroupRepository;
import com.example.util.domain.vo.PagingAndSorting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Created by Abdul on 19/5/16.
 */
@Service
public class GroupServiceImpl implements GroupService {

    @Autowired
    private GroupRepository groupRepository;

    @Override
    public Iterable<Group> findAll() throws Exception {
        return groupRepository.findAll();
    }

    @Override
    public Page<Group> findAll(PagingAndSorting pagingAndSorting) throws Exception {
        return groupRepository.findAll(pagingAndSorting.toPageRequest());
    }

    @Override
    public Page<Group> findAllByDomain(PagingAndSorting pagingAndSorting, Domain domain) {
        return groupRepository.findAllByDomain(pagingAndSorting.toPageRequest(), domain);
    }

    @Override
    public Page<Group> findAllByDeleted(PagingAndSorting pagingAndSorting, boolean deleted) {
        return groupRepository.findAllByDeleted(pagingAndSorting.toPageRequest(), deleted);
    }

    @Override
    public Iterable<Group> findAllByDeleted(boolean deleted) {
        return groupRepository.findAllByDeleted(deleted);
    }

    @Override
    public Group find(Long id) throws Exception {
        return groupRepository.findOne(id);
    }

    @Override
    public Group create(Group group) throws Exception {
        return groupRepository.save(group);
    }

    @Override
    public Group update(Group group) throws Exception {
        return groupRepository.save(group);
    }

    @Override
    public void delete(Group group) throws Exception {
        groupRepository.delete(group);
    }

    @Override
    public void delete(Long id) throws Exception {
        groupRepository.delete(id);
    }

    @Override
    public Iterable<Group> findAllByDomain(Domain domain) {
        return groupRepository.findAllByDomain(domain);
    }

    @Override
    public Boolean isGroupExist(String username, Domain domain) {

        Boolean isGroupExist = false;
        Group existingGroup = groupRepository.findByNameAndDomain(username, domain);

        if(existingGroup != null) {
            isGroupExist = true;
        }
        return  isGroupExist;
    }


}
