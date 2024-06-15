package com.example.pets.dto.request;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class ClientRequest extends Request {

    private OwnerRequest owner;
    @Valid
    @NotNull(message = "Pets must be not empty")
    @Size(min = 1, message = "Min size is one")
    private List<PetRequest> pets;
    private LocalDateTime registeredAt;
}
