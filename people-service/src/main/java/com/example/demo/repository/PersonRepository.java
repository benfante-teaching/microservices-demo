package com.example.demo.repository;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.model.Person;

public interface PersonRepository extends JpaRepository<Person, Long> {
    Iterable<Person> findByFirstNameContainingOrLastNameContainingAllIgnoreCaseOrderByLastNameAscFirstNameAsc(String firstName, String lastName);

    Optional<Person> findByExternalId(UUID uuid);

    void deleteByExternalId(UUID id);
}