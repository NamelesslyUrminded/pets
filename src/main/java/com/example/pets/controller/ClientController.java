package com.example.pets.controller;

import com.example.pets.dto.request.ClientRequest;
import com.example.pets.dto.response.ClientResponse;
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

    @PostMapping
    public ResponseEntity<ClientResponse> create(@Valid @RequestBody ClientRequest clientRequest) {
        return ResponseEntity.ok(clientService.create(clientRequest));
    }

    @GetMapping({"/{clientId}"})
    public ResponseEntity findById(@PathVariable Long clientId) {
        return ResponseEntity.ok(clientService.findById(clientId));
    }

    @DeleteMapping
    public ResponseEntity deleteByList(@RequestBody List<Long> idList) {
        clientService.deleteSeveral(idList);
        return ResponseEntity.ok().build();
    }

}
