package com.example.pets.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientResponse extends Response {

    private OwnerResponse owner;
    private List<PetResponse> pets;

}
