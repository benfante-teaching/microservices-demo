package com.example.demo.controller;

import java.util.UUID;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
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
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

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
        @Operation(description = "Questo funzione restituisce l'elenco delle persone, permettendo di filtrarle per nome e cognome")
        public Iterable<PersonDto> listAllPerson(
                        @RequestParam(name = "q", defaultValue = "") String filter) {
                log.debug("Getting people using filter: {}", filter);
                return StreamSupport.stream(peopleService.getAllPerson(filter).spliterator(), true)
                                .map(personConverter::convert).toList();
        }

        @GetMapping("/{id}")
        @Operation(summary = "Get a person by its id")
        @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Found the person",
                        content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                        schema = @Schema(implementation = PersonDto.class))}),
                        @ApiResponse(responseCode = "400", description = "Invalid id supplied",
                                        content = @Content),
                        @ApiResponse(responseCode = "404", description = "Person not found",
                                        content = @Content(
                                                        mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE,
                                                        schema = @Schema(
                                                                        implementation = ProblemDetail.class)))})
        public PersonDto getByUuid(@Parameter(description = "The person id") @PathVariable(
                        name = "id") UUID id) {
                log.debug("Getting person with id: {}", id);
                return personConverter.convert(peopleService.findPersonByUuid(id));
        }

        @PostMapping
        @ResponseStatus(HttpStatus.CREATED)
        public PersonDto addPerson(@RequestBody PersonDto person) {
                log.debug("Adding person: {}", person);
                return personConverter.convert(
                                peopleService.addPerson(personDtoConverter.convert(person)));
        }

        @PatchMapping("/{id}")
        @Operation(summary = "Update a person by its id")
        @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Person updated",
                        content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                        schema = @Schema(implementation = PersonDto.class))}),
                        @ApiResponse(responseCode = "400", description = "Invalid id supplied",
                                        content = @Content),
                        @ApiResponse(responseCode = "404", description = "Person not found",
                                        content = @Content(
                                                        mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE,
                                                        schema = @Schema(
                                                                        implementation = ProblemDetail.class)))})
        public PersonDto updatePerson(@Parameter(description = "The person id") @PathVariable(
                        name = "id") UUID id, @RequestBody PersonDto person) {
                log.debug("Updating person with id: {} and data: {}", id, person);
                person = person.withUuid(id); // assuring the correct id is set
                return personConverter.convert(
                                peopleService.updatePerson(personDtoConverter.convert(person)));
        }

        @DeleteMapping("/{id}")
        @ResponseStatus(HttpStatus.NO_CONTENT)
        @Operation(summary = "Delete a person by its id")
        @ApiResponses(value = {@ApiResponse(responseCode = "204", description = "Person deleted",
                        content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                        schema = @Schema(implementation = Void.class))}),
                        @ApiResponse(responseCode = "400", description = "Invalid id supplied",
                                        content = @Content),
                        @ApiResponse(responseCode = "404", description = "Person not found",
                                        content = @Content(
                                                        mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE,
                                                        schema = @Schema(
                                                                        implementation = ProblemDetail.class)))})
        public void deletePerson(@Parameter(description = "The person id") @PathVariable(
                        name = "id") UUID id) {
                log.debug("Deleting person with id: {}", id);
                peopleService.deletePerson(id);
        }
}
