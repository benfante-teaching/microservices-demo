package com.example.demoweb.model;

import java.util.UUID;

public class Person {
    private UUID uuid;
    private String firstName;
    private String lastName;

    public Person() {
    }

    public Person(UUID uuid, String firstName, String lastName, int age) {
        this.uuid = uuid;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

}