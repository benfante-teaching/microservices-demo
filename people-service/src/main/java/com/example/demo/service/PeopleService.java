package com.example.demo.service;

import java.util.Objects;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.demo.model.Person;
import com.example.demo.repository.PersonRepository;
import com.example.demo.util.NotFoundException;

@Service
@Transactional(readOnly = true)
public class PeopleService {

    @Autowired
    private PersonRepository personRepository;

    public Iterable<Person> getAllPerson(String filter) {
        return personRepository
                .findByFirstNameContainingOrLastNameContainingAllIgnoreCaseOrderByLastNameAscFirstNameAsc(
                        filter, filter);
    }

    public Person findPersonById(Long id) {
        return personRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Person not found with id: %s".formatted(id)));
    }

    public Person findPersonByUuid(UUID uuid) {
        return personRepository.findByExternalId(uuid).orElseThrow(
                () -> new NotFoundException("Person not found with uuid: %s".formatted(uuid)));
    }

    @Transactional(readOnly = false)
    public Person addPerson(Person person) {
        Person result = personRepository.save(person);
        personRepository.flush();
        return result;
    }

    @Transactional(readOnly = false)
    public Person updatePerson(Person person) {
        Person dbPerson = personRepository.findByExternalId(person.getExternalId())
                .orElseThrow(() -> new NotFoundException(
                        "Person not found with uuid: %s".formatted(person.getExternalId())));
        if (Objects.nonNull(person.getFirstName())) {
            dbPerson.setFirstName(person.getFirstName());
        }
        if (Objects.nonNull(person.getLastName())) {
            dbPerson.setLastName(person.getLastName());
        }
        personRepository.flush();
        return dbPerson;
    }

    @Transactional(readOnly = false)
    public void deletePerson(UUID id) {
        findPersonByUuid(id); // Check if the person exists
        personRepository.deleteByExternalId(id);
    }
}
