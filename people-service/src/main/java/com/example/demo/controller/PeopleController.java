package com.example.demo.controller;

import java.util.UUID;
import java.util.stream.StreamSupport;
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

import com.example.demo.model.PersonDto;
import com.example.demo.model.converter.PersonDtoToPersonConverter;
import com.example.demo.model.converter.PersonToPersonDtoConverter;
import com.example.demo.service.PeopleService;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/api/v1/people")
public class PeopleController {
    private static final Logger log = LoggerFactory.getLogger(PeopleController.class);

    @Autowired
    private PeopleService peopleService;
    @Autowired
    private PersonToPersonDtoConverter personConverter;
    @Autowired
    private PersonDtoToPersonConverter personDtoConverter;

    @GetMapping
    @Operation(
            description = "Questo funzione restituisce l'elenco delle persone, permettendo di filtrarle per nome e cognome")
    public Iterable<PersonDto> listAllPerson(
            @RequestParam(name = "q", defaultValue = "") String filter) {
        log.debug("Getting people using filter: {}", filter);
        return StreamSupport.stream(peopleService.getAllPerson(filter).spliterator(), true)
                .map(personConverter::convert).toList();
    }

    @GetMapping("/{id}")
    public PersonDto getByUuid(@PathVariable(name = "id") UUID id) {
        return personConverter.convert(peopleService.findPersonByUuid(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PersonDto addPerson(@RequestBody PersonDto person) {
        return personConverter.convert(peopleService.addPerson(personDtoConverter.convert(person)));
    }
}
