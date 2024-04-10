package com.example.pets.service;

import com.example.pets.dto.request.PetRequest;
import com.example.pets.entity.Pet;
import com.example.pets.repository.PetRepository;
import com.example.pets.service.mapper.RequestMapper;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class PetService {
    private final PetRepository petRepository;
    private final ModelMapper modelMapper;

    private final RequestMapper<Pet, PetRequest> requestMapper;

    public Pet update(PetRequest petRequest){
        Assert.notNull(petRequest, "Pet request is null");
        Pet entity = Objects.isNull(petRequest.getId()) ? new Pet() : findById(petRequest.getId());
        requestMapper.toEntity(entity, petRequest);
        return petRepository.save(entity);
    }

    public boolean delete(Long id){
        Assert.notNull(id, "Pet's id is null");
        this.petRepository.deleteById(id);
        return true;
    }

    public Pet findById(Long id) {
        Assert.notNull(id, "Pet's id is null");
        return this.petRepository.findById(id).orElseThrow(()-> new RuntimeException());
    }


}
