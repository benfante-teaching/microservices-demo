package com.example.demoweb.service;

import java.util.Arrays;
import java.util.Collections;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
// import com.netflix.discovery.EurekaClient;
import com.example.demoweb.model.Person;
import com.example.demoweb.model.Result;

@Service
public class PeopleService {
    public static final int CODE_SUCCESS = 0;
    public static final int CODE_ERROR = 1;
    public static final int CODE_SERVICE_NOT_AVAILABLE = 2;

    private static final String PEOPLE_SERVICE_URL = "http://DEMOPERSON/api/v1/people";

    // Uncomment the following line if you want to use EurekaClient
    // @Autowired
    // private EurekaClient eurekaClient;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private CircuitBreakerFactory<?, ?> circuitBreakerFactory;

    public Result<Iterable<Person>> findPeople(String filter) {
        CircuitBreaker cb = circuitBreakerFactory.create("peopleServiceCB");

        Result<Iterable<Person>> result = cb.run(() -> {
            /* *** Uncomment this block to directly use Eureka client to get the service URL *** */
            // InstanceInfo peopleService = eurekaClient.getNextServerFromEureka("DEMOPERSON",
            // false);
            // String peopleServiceUrl = peopleService.getHomePageUrl();
            // model.addAttribute("peopleServiceUrl", peopleServiceUrl);
            // return restTemplate.getForObject(peopleServiceUrl+"/api/v1/people", Person[].class);

            /*
             * *** Comment this block if you want to directly use Eureka client to get the service
             * URL ***
             */
            UriComponentsBuilder builder =
                    UriComponentsBuilder.fromUriString(PEOPLE_SERVICE_URL).queryParam("q", filter);
            Person[] people = restTemplate.getForObject(builder.toUriString(), Person[].class);
            return new Result<Iterable<Person>>(Arrays.asList(people));
        }, (throwable) -> {
            return new Result<>(Collections.emptyList(), CODE_SERVICE_NOT_AVAILABLE, throwable,
                    "People Service is not available.");
        });
        return result;
    }

    public Result<Person> findPerson(UUID uuid) {
        CircuitBreaker cb = circuitBreakerFactory.create("peopleServiceCB");

        Result<Person> result = cb.run(() -> {
            Person person =
                    restTemplate.getForObject(PEOPLE_SERVICE_URL + "/{id}", Person.class, uuid);
            return new Result<>(person);
        }, (throwable) -> {
            return new Result<>(null, CODE_SERVICE_NOT_AVAILABLE, throwable,
                    "People Service is not available.");
        });
        return result;
    }

    public Result<Person> save(Person person) {
        CircuitBreaker cb = circuitBreakerFactory.create("peopleServiceCB");

        Result<Person> result = cb.run(() -> {
            Person savedPerson = null;
            if (person.getUuid() == null) {
                savedPerson = restTemplate.postForObject(PEOPLE_SERVICE_URL, person, Person.class);
            } else {
                savedPerson = restTemplate.patchForObject(PEOPLE_SERVICE_URL + "/{id}", person,
                        Person.class, person.getUuid());
            }
            return new Result<>(savedPerson);
        }, (throwable) -> {
            return new Result<>(null, CODE_SERVICE_NOT_AVAILABLE, throwable,
                    "People Service is not available.");
        });
        return result;
    }

    public Result<Person> delete(UUID id) {
        CircuitBreaker cb = circuitBreakerFactory.create("peopleServiceCB");

        Result<Person> result = cb.run(() -> {
            restTemplate.delete(PEOPLE_SERVICE_URL + "/{id}", id);
            return new Result<>(null);
        }, (throwable) -> {
            return new Result<>(null, CODE_SERVICE_NOT_AVAILABLE, throwable,
                    "People Service is not available.");
        });
        return result;
    }
}
