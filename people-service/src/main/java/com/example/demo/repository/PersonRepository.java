package com.example.demo.repository;

import org.springframework.data.repository.CrudRepository;

import com.example.demo.model.Person;

public interface PersonRepository extends CrudRepository<Person, Long> {
    Iterable<Person> findByFirstNameContainingOrLastNameContainingAllIgnoreCaseOrderByLastNameAscFirstNameAsc(String firstName, String lastName);
}