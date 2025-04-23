package com.example.demo.repository;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.iterableWithSize;
import static org.hamcrest.Matchers.notNullValue;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;
import com.example.demo.TestDemoApplication;
import com.example.demo.model.Person;

@SpringBootTest
@Import(TestDemoApplication.class)
@Transactional
public class PersonRepositoryTest {
    @Autowired
    private PersonRepository personRepository;

    @Test
    void testFindAll() {
        Iterable<Person> result = personRepository.findAll();
        assertThat(result, iterableWithSize(4));
    }

    @Test
    void testFindByFirstNameContainingOrLastNameContainingAllIgnoreCaseOrderByLastNameAscFirstNameAsc() {
        Iterable<Person> result =
                personRepository.findByFirstNameContainingOrLastNameContainingAllIgnoreCaseOrderByLastNameAscFirstNameAsc("Mario", "NotExistant");
        assertThat(result, iterableWithSize(2));

    }

    @Test
    void testSearchWithEmptyResult() {
        Iterable<Person> result =
                personRepository.findByFirstNameContainingOrLastNameContainingAllIgnoreCaseOrderByLastNameAscFirstNameAsc("NotExistant", "NotExistant");
        assertThat(result, notNullValue());
        assertThat(result, iterableWithSize(0));
    }

    @Test
    void testFindByUuid() {
        final String uuid = "00000000-0000-0000-0000-000000000003";
        Optional<Person> result = personRepository.findByExternalId(UUID.fromString(uuid));
        assertThat(result, notNullValue());
        assertThat(result.isPresent(), equalTo(true));
        assertThat(result.get().getId(), equalTo(10002L));
        assertThat(result.get().getExternalId(), equalTo(UUID.fromString(uuid)));
        assertThat(result.get().getFirstName(), equalTo("Carlo"));
        assertThat(result.get().getLastName(), equalTo("Neri"));
    }

    @Test
    void testFindByUuidNotFound() {
        final String uuid = "00000000-0000-0000-0000-000000000999";
        Optional<Person> result = personRepository.findByExternalId(UUID.fromString(uuid));
        assertThat(result, notNullValue());
        assertThat(result.isPresent(), equalTo(false));
    }

    @Test
    void testDeleteByExternalId() {
        final String uuid = "00000000-0000-0000-0000-000000000001";
        personRepository.deleteByExternalId(UUID.fromString(uuid));
        Optional<Person> result = personRepository.findByExternalId(UUID.fromString(uuid));
        assertThat(result, notNullValue());
        assertThat(result.isPresent(), equalTo(false));
    }

    @Test
    void testDeleteByExternalIdNotFound() {
        final String uuid = "00000000-0000-0000-0000-000000000999";
        personRepository.deleteByExternalId(UUID.fromString(uuid));
        Optional<Person> result = personRepository.findByExternalId(UUID.fromString(uuid));
        assertThat(result, notNullValue());
        assertThat(result.isPresent(), equalTo(false));
    }
}
