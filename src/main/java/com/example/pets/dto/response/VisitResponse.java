package com.example.pets.dto.response;

import lombok.Data;

import java.io.Serializable;

@Data
public class VisitResponse extends Response {
    private String description;
}
