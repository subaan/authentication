package com.example.service;

import com.example.model.Domain;
import com.example.model.Group;
import com.example.model.User;
import com.example.util.domain.CRUDService;
import com.example.util.domain.vo.PagingAndSorting;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Created by Abdul on 19/5/16.
 */
@Service
public interface GroupService extends CRUDService<Group> {

    /**
     * This method find domain with specified domain name.
     *
     * @param domain the domain
     * @return Domain.
     */
    Iterable<Group> findAllByDomain(Domain domain);

    /**
     * This method is used to return the user list by domain.
     * @param pagingAndSorting
     * @param domain the domain object.
     *
     * @return entity
     * @throws Exception if error occurs
     */
    Page<Group> findAllByDomain(PagingAndSorting pagingAndSorting, Domain domain);

    /**
     * This method get all group with specified by deleted value.
     *
     * @param deleted the domain
     * @return the group list.
     */
    Iterable<Group> findAllByDeleted(boolean deleted);

    /**
     * This method is group to return the user list by deleted value.
     *
     * @param pagingAndSorting
     * @param deleted is group is soft deleted.
     *
     * @return entity
     * @throws Exception if error occurs
     */
    Page<Group> findAllByDeleted(PagingAndSorting pagingAndSorting, boolean deleted);

    /**
     * This method is used to return boolean value if group name already exist in the domain.
     *
     * @param name the group name
     * @param domain the domain.
     * @return the 'true' or false. If user already exist  in domain return true.
     */
    public Boolean isGroupExist(String name, Domain domain);
}
