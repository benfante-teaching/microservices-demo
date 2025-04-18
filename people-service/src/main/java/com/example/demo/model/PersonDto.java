package com.example.demo.model;

import java.util.UUID;

public record PersonDto(UUID uuid, String firstName, String lastName) {
    
}