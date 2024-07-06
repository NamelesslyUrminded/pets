package com.example.pets.controller;

import com.example.pets.dto.request.ClientRequest;
import com.example.pets.dto.response.ClientResponse;
import com.example.pets.entity.Client;
import com.example.pets.exception.EntityModifyException;
import com.example.pets.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin(origins = {"*"})
@RestController
@RequestMapping({"/client"})
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;

    //mappers

    @PostMapping
    public ResponseEntity<List<ClientResponse>> create(@Valid @RequestBody ClientRequest clientRequest) throws EntityModifyException {
      List<Client> clients = clientService.create(clientRequest);
      return ResponseEntity.ok(clientService.toResponseList(clients));

    }

    @GetMapping({"/{clientId}"})
    public ResponseEntity<ClientResponse> findById(@PathVariable Long clientId) {
        return ResponseEntity.ok(clientService.findById(clientId));
    }
    @GetMapping
    public ResponseEntity<Object> getRandomClients(){
      clientService.fillClients();
      return ResponseEntity.ok().build();
    }
}
