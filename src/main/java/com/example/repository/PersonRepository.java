package com.example.repository;

/**
 * Created by Abdul on 18/5/16.
 */
import com.example.model.Person;
import org.springframework.data.repository.CrudRepository;

public interface PersonRepository extends CrudRepository<Person, Long> {}