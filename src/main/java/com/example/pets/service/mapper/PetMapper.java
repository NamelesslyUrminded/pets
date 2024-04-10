package com.example.pets.service.mapper;

import com.example.pets.dto.request.PetRequest;
import com.example.pets.dto.response.PetResponse;
import com.example.pets.entity.Pet;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PetMapper implements RequestMapper<Pet, PetRequest>, ResponseMapper<Pet, PetResponse> {

    private final ModelMapper modelMapper;

    @Override
    public Pet toEntity(Pet pet, PetRequest petRequest) {
        modelMapper.map(petRequest, pet);
        return pet;
    }

    @Override
    public PetResponse toResponse(Pet pet) {
        PetResponse petResponse = new PetResponse();
        modelMapper.map(pet, petResponse);
        return petResponse;
    }
}
