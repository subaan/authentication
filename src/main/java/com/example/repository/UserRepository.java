package com.example.repository;

import com.example.model.User;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by Abdul on 19/5/16.
 */
public interface UserRepository extends CrudRepository<User, Long> {}
