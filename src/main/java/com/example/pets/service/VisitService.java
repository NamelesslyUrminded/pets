package com.example.pets.service;

import com.example.pets.dto.VisitDto;
import com.example.pets.dto.request.VisitRequest;
import com.example.pets.dto.response.VisitResponse;
import com.example.pets.entity.Visit;
import com.example.pets.repository.VisitRepository;
import com.example.pets.service.mapper.RequestMapper;
import com.example.pets.service.mapper.ResponseMapper;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class VisitService {
    private final VisitRepository visitRepository;
    private final RequestMapper<Visit, VisitRequest> requestMapper;
    private final ResponseMapper<Visit, VisitResponse> responseMapper;

    public Visit save(VisitRequest visitRequest){
        Objects.requireNonNull(visitRequest, "request is null");
        Visit entity = Objects.isNull(visitRequest.getId()) ? new Visit() : findById(visitRequest.getId());
        entity = requestMapper.toEntity(entity, visitRequest);
        return entity;
    }

    public boolean delete(Long id){
        Objects.requireNonNull(id, "Id must not be null");
        this.visitRepository.deleteById(id);
        return true;
    }

    public Visit findById(Long id) {
        if (Objects.isNull(id)){
            return new Visit();
        }
        return this.visitRepository.findById(id).orElse(new Visit());
    }

}
