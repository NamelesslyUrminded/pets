package com.example.pets.dto.filter;

import lombok.Data;

@Data
public class OwnerFilter {

    private String name;
    private FilterArgument nameArg;
    private String surname;
    private FilterArgument surnameArg;
}
