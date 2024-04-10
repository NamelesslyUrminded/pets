package com.example.pets.service.mapper;

import com.example.pets.dto.request.ClientRequest;
import com.example.pets.dto.response.ClientResponse;
import com.example.pets.dto.response.OwnerResponse;
import com.example.pets.dto.response.PetResponse;
import com.example.pets.entity.Client;
import com.example.pets.entity.Owner;
import com.example.pets.entity.Pet;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
@RequiredArgsConstructor
public class ClientMapper implements RequestMapper<Client, ClientRequest>, ResponseMapper<Client, ClientResponse> {
    private final ResponseMapper<Owner, OwnerResponse> ownerResponseMapper;
    private final ResponseMapper<Pet, PetResponse> petResponseMapper;

    @Override
    public Client toEntity(Client client, ClientRequest clientRequest) {
        if (clientRequest.getRegisteredAt() != null)
            client.setRegisteredAt(clientRequest.getRegisteredAt());
        return client;
    }

    @Override
    public ClientResponse toResponse(Client client) {
        ClientResponse clientResponse = new ClientResponse();
        clientResponse.setOwner(ownerResponseMapper.toResponse(client.getOwner()));
        clientResponse.setPets(Collections.singletonList((petResponseMapper.toResponse(client.getPet()))));
        return clientResponse;
    }

}
