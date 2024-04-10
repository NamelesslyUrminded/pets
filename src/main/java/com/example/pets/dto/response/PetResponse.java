package com.example.pets.dto.response;

import lombok.Data;

@Data
public class PetResponse extends Response{

    private Long id;
    private String name;
}
