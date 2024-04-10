package com.example.pets.dto.filter;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Data
public class ClientFilter {

    private String name;
    @Valid
    @NotNull(message = "name arg is null")
    private FilterArgument nameArg;

    private String surname;
    @Valid
    @NotNull(message = "kek")
    private FilterArgument surnameArg;
}
