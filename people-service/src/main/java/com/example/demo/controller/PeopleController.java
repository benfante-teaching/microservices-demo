package com.example.demo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Person;
import com.example.demo.service.PeopleService;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/api/v1/people")
public class PeopleController {
    private static final Logger log = LoggerFactory.getLogger(PeopleController.class);

    @Autowired
    private PeopleService peopleService;
    
    @GetMapping
    @Operation(description = "Questo funzione restituisce l'elenco delle persone, permettendo di filtrarle per nome e cognome")
    public Iterable<Person> listAllPerson(@RequestParam(name="q", defaultValue = "") String filter) {
        return peopleService.getAllPerson(filter);
    }

    @GetMapping("/{id}")
    public Person getById(@PathVariable(name="id") Long id) {
        return peopleService.findPersonById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Person addPerson(@RequestBody Person person) {
        return peopleService.addPerson(person);
    }
}