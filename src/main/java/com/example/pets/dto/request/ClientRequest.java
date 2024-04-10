package com.example.pets.dto.request;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class ClientRequest extends Request {

    private OwnerRequest owner;
    private List<PetRequest> pets;
    @Valid
    @NotBlank
    private LocalDateTime registeredAt;
}
