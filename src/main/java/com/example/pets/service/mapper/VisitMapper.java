package com.example.pets.service.mapper;

import com.example.pets.dto.request.VisitRequest;
import com.example.pets.dto.response.VisitResponse;
import com.example.pets.entity.Visit;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class VisitMapper implements RequestMapper<Visit, VisitRequest>, ResponseMapper<Visit, VisitResponse> {

    private final ModelMapper modelMapper;

    @Override
    public Visit toEntity(Visit visit, VisitRequest visitRequest) {
        modelMapper.map(visitRequest, visit);
        return visit;
    }

    @Override
    public VisitResponse toResponse(Visit visit) {
        VisitResponse visitResponse = new VisitResponse();
        modelMapper.map(visit, visitResponse);
        return visitResponse;
    }
}
