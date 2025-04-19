package com.example.demo.model.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import com.example.demo.model.Person;
import com.example.demo.model.PersonDto;

@Component
public class PersonToPersonDtoConverter implements Converter<Person, PersonDto> {

    @Override
    public PersonDto convert(Person person) {
        if (person == null) {
            return null;
        }
        return new PersonDto(person.getExternalId(), person.getFirstName(), person.getLastName());
    }
    
}