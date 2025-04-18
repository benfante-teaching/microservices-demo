package com.example.demo.model.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import com.example.demo.model.Person;
import com.example.demo.model.PersonDto;

@Component
public class PersonDtoToPersonConverter implements Converter<PersonDto, Person> {

    @Override
    public Person convert(PersonDto source) {
        if (source == null) {
            return null;
        }
        return  new Person(null, source.uuid(), source.firstName(), source.lastName());
    }
    
}