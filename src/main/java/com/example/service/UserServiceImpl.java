package com.example.service;

import com.example.model.Domain;
import com.example.model.User;
import com.example.repository.DomainRepository;
import com.example.repository.UserRepository;
import com.example.util.domain.vo.PagingAndSorting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

/**
 * Created by Abdul on 19/5/16.
 */
@Service
public class UserServiceImpl implements UserService {

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

}
