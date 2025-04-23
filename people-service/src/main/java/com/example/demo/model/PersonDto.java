package com.example.demo.model;

import java.util.UUID;

public record PersonDto(UUID uuid, String firstName, String lastName) {
    public PersonDto withUuid(UUID uuid) {
        return new PersonDto(uuid, firstName, lastName);
    }
}