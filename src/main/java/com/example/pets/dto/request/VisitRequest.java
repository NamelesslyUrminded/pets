package com.example.pets.dto.request;

import lombok.Data;

@Data
public class VisitRequest extends Request {

    private String description;
    private Long id;

}
