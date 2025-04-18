package com.example.demo.service;

import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.Person;
import com.example.demo.repository.PersonRepository;
import com.example.demo.util.NotFoundException;

@Service
public class PeopleService {

    @Autowired
    private PersonRepository personRepository;

    public Iterable<Person> getAllPerson(String filter) {
        return personRepository.findByFirstNameContainingOrLastNameContainingAllIgnoreCaseOrderByLastNameAscFirstNameAsc(filter, filter);
    }

    public Person findPersonById(Long id) {
        return personRepository.findById(id).orElseThrow(() -> new NotFoundException("Person not found with id: %s".formatted(id)));
    }

    public Person findPersonByUuid(UUID uuid) {
        return personRepository.findByUuid(uuid).orElseThrow(() -> new NotFoundException("Person not found with uuid: %s".formatted(uuid)));
    }

    public Person addPerson(Person person) {
        return personRepository.save(person);
    }
}