package com.example.demo.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.iterableWithSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThrows;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;
import com.example.demo.TestDemoApplication;
import com.example.demo.model.Person;
import com.example.demo.util.NotFoundException;

@SpringBootTest
@Import(TestDemoApplication.class)
@Transactional
public class PeopleServiceTest {
    @Autowired
    private PeopleService peopleService;

    @Test
    void testGetAllPerson() {
        Iterable<Person> result = peopleService.getAllPerson("");
        assertThat(result, iterableWithSize(4));
    }

    @Test
    void testGetAllMario() {
        Iterable<Person> result = peopleService.getAllPerson("Mario");
        assertThat(result, iterableWithSize(2));
        result.forEach(person -> {
            assertThat(person.getFirstName(), equalTo("Mario"));
        });
    }

    @Test
    void testFindPersonById() {
        final String uuid = "00000000-0000-0000-0000-000000000003";
        final Long id = 10002L;
        Person result = peopleService.findPersonById(id);
        assertThat(result, notNullValue());
        assertThat(result.getId(), equalTo(id));
        assertThat(result.getExternalId(), equalTo(UUID.fromString(uuid)));
        assertThat(result.getFirstName(), equalTo("Carlo"));
        assertThat(result.getLastName(), equalTo("Neri"));
    }

    @Test
    void testFindPersonByIdNotFound() {
        final Long id = 99999L;
        assertThrows(NotFoundException.class, () -> {
            peopleService.findPersonById(id);
        });
    }

    @Test
    void testFindPersonByUuid() {
        final String uuid = "00000000-0000-0000-0000-000000000003";
        Person result = peopleService.findPersonByUuid(UUID.fromString(uuid));
        assertThat(result, notNullValue());
        assertThat(result.getId(), equalTo(10002L));
        assertThat(result.getExternalId(), equalTo(UUID.fromString(uuid)));
        assertThat(result.getFirstName(), equalTo("Carlo"));
        assertThat(result.getLastName(), equalTo("Neri"));
    }

    @Test
    void testAddPerson() {
        Person person = new Person(null, null, "New first name", "New last name");
        Person result = peopleService.addPerson(person);
        assertThat(result, notNullValue());
        assertThat(result.getId(), notNullValue());
        assertThat(result.getExternalId(), notNullValue());
        assertThat(result.getFirstName(), equalTo("New first name"));
        assertThat(result.getLastName(), equalTo("New last name"));
        Person newPerson = peopleService.findPersonById(result.getId());
        assertThat(newPerson, notNullValue());
        assertThat(newPerson.getId(), equalTo(result.getId()));
        assertThat(newPerson.getExternalId(), equalTo(result.getExternalId()));
        assertThat(newPerson.getFirstName(), equalTo(result.getFirstName()));
        assertThat(newPerson.getLastName(), equalTo(result.getLastName()));
    }

    @Test
    void testUpdatePerson() {
        final String uuid = "00000000-0000-0000-0000-000000000003";
        Person person = new Person(null, UUID.fromString(uuid), "Updated first name", "Updated last name");
        Person updatedPerson = peopleService.updatePerson(person);
        assertThat(updatedPerson, notNullValue());
        assertThat(updatedPerson.getId(), equalTo(10002L));
        assertThat(updatedPerson.getExternalId(), equalTo(UUID.fromString(uuid)));
        assertThat(updatedPerson.getFirstName(), equalTo("Updated first name"));
        assertThat(updatedPerson.getLastName(), equalTo("Updated last name"));
        Person result = peopleService.findPersonByUuid(UUID.fromString(uuid));
        assertThat(result, notNullValue());
        assertThat(result.getId(), equalTo(10002L));
        assertThat(result.getExternalId(), equalTo(UUID.fromString(uuid)));
        assertThat(result.getFirstName(), equalTo("Updated first name"));
        assertThat(result.getLastName(), equalTo("Updated last name"));
    }

    @Test
    void testDeletePerson() {
        final String uuid = "00000000-0000-0000-0000-000000000003";
        peopleService.deletePerson(UUID.fromString(uuid));
        assertThrows(NotFoundException.class, () -> {
            peopleService.findPersonByUuid(UUID.fromString(uuid));
        });
    }

    @Test
    void testDeletePersonNotFound() {
        final String uuid = "00000000-0000-0000-0000-000000000999";
        assertThrows(NotFoundException.class, () -> {
            peopleService.deletePerson(UUID.fromString(uuid));
        });
    }

}
