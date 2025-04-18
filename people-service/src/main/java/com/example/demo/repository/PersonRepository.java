package com.example.demo.repository;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.repository.CrudRepository;

import com.example.demo.model.Person;

public interface PersonRepository extends CrudRepository<Person, Long> {
    Iterable<Person> findByFirstNameContainingOrLastNameContainingAllIgnoreCaseOrderByLastNameAscFirstNameAsc(String firstName, String lastName);

    Optional<Person> findByUuid(UUID uuid);
}