package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.Person;
import com.example.demo.repository.PersonRepository;

@Service
public class PeopleService {

    @Autowired
    private PersonRepository personRepository;

    public Iterable<Person> getAllPerson(String filter) {
        return personRepository.findByFirstNameContainingOrLastNameContainingAllIgnoreCaseOrderByLastNameAscFirstNameAsc(filter, filter);
    }

    public Person findPersonById(Long id) {
        return personRepository.findById(id).get();
    }

    public Person addPerson(Person person) {
        return personRepository.save(person);
    }
}