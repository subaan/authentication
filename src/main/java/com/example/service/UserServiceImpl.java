package com.example.service;

import com.example.model.Domain;
import com.example.model.User;
import com.example.repository.DomainRepository;
import com.example.repository.UserRepository;
import com.example.util.domain.vo.PagingAndSorting;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.ldap.core.ContextMapper;
import org.springframework.ldap.core.DirContextAdapter;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Abdul on 19/5/16.
 */
@Service
public class UserServiceImpl implements UserService {

    /** Logger constant. */
    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserRepository userRepository;

    @Override
    public Iterable<User> findAll() throws Exception {
        return userRepository.findAll();
    }

    @Override
    public Page<User> findAll(PagingAndSorting pagingAndSorting) throws Exception {
        return userRepository.findAll(pagingAndSorting.toPageRequest());
    }

    @Override
    public Page<User> findAllByDomain(PagingAndSorting pagingAndSorting, Domain domain) {
        return userRepository.findAllByDomain(pagingAndSorting.toPageRequest(),domain);
    }

    @Override
    public List<User> findAllByIds(List<Long> ids) {
        return userRepository.findAllByIds(ids);
    }

    @Override
    public Page<User> findAllByDeleted(PagingAndSorting pagingAndSorting, boolean deleted) {
        return userRepository.findAllByDeleted(pagingAndSorting.toPageRequest(), deleted);
    }

    @Override
    public Iterable<User> findAllByDeleted(boolean deleted) {
        return userRepository.findAllByDeleted(deleted);
    }

    @Override
    public User find(Long id) throws Exception {
        return userRepository.findOne(id);
    }

    @Override
    public User create(User user) throws Exception {
        return userRepository.save(user);
    }

    @Override
    public User update(User user) throws Exception {
        return userRepository.save(user);
    }

    @Override
    public void delete(User User) throws Exception {

    }

    @Override
    public Iterable<User> findAllByDomain(Domain domain) {
        return userRepository.findAllByDomain(domain);
    }

    @Override
    public void delete(Long id) throws Exception {
        userRepository.delete(id);
    }

    @Override
    public User findByUsername(String  username) {
        return userRepository.findFirstByUsername(username);
    }

    @Override
    public User findByUsernameAndDomain(String username, Domain domain) {
        return userRepository.findByUsernameAndDomain(username, domain);
    }

    @Override
    public User findByUsernameAndPassword(String  username, String password) {
        return userRepository.findByUsernameAndPassword(username, password);
    }

    @Override
    public User findByUsernameAndPasswordAndDomain(String username, String password, Domain domain) {
        return userRepository.findByUsernameAndPasswordAndDomain(username, password, domain);
    }

    @Override
    public Boolean isUserExist(String username, Domain domain) {

        Boolean isUserExist = false;
        User existingUser = this.findByUsernameAndDomain(username, domain);

        if(existingUser != null) {
            isUserExist = true;
        }
        return  isUserExist;
    }

}
