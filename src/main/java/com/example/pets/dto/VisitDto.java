package com.example.pets.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class VisitDto {

    private Long id;

    private LocalDate visitDate;

    private String description;

    private List<VisitDto> visits;

}
