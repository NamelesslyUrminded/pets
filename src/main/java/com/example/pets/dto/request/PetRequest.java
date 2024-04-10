package com.example.pets.dto.request;

import lombok.Data;

@Data
public class PetRequest extends Request {
    private Long id;
    private String name;
}
