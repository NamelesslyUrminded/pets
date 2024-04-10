package com.example.pets.service.mapper;

import com.example.pets.dto.request.OwnerRequest;
import com.example.pets.dto.response.OwnerResponse;
import com.example.pets.entity.Owner;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OwnerMapper implements RequestMapper<Owner, OwnerRequest>, ResponseMapper<Owner, OwnerResponse> {

    private final ModelMapper modelMapper;

    @Override
    public Owner toEntity(Owner owner, OwnerRequest ownerRequest) {
        modelMapper.map(ownerRequest, owner);
        return owner;
    }

    @Override
    public OwnerResponse toResponse(Owner owner) {
        OwnerResponse ownerResponse = new OwnerResponse();
        modelMapper.map(owner, ownerResponse);
        return ownerResponse;
    }

}
