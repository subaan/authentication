package com.example.repository;

import com.example.model.Domain;
import com.example.model.Group;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Created by Abdul on 19/5/16.
 */
public interface GroupRepository extends PagingAndSortingRepository<Group, Long> {

    /**
     * This method get all group with specified domain name.
     *
     * @param domain the domain
     * @return the group list.
     */
    Iterable<Group> findAllByDomain(Domain domain);

    /**
     * This method is group to return the user list by domain.
     *
     * @param pageable the pageable object.
     * @param domain the domain object.
     *
     * @return entity
     * @throws Exception if error occurs
     */
    Page<Group> findAllByDomain(Pageable pageable, Domain domain);

    /**
     * This method find Group with specified name.
     *
     * @param name the group name.
     * @param domain the domain object.
     * @return User.
     */
    Group findByNameAndDomain(String name, Domain domain);

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
     * @param pageable the pageable object.
     * @param deleted is group is soft deleted.
     *
     * @return entity
     * @throws Exception if error occurs
     */
    Page<Group> findAllByDeleted(Pageable pageable, boolean deleted);

}
