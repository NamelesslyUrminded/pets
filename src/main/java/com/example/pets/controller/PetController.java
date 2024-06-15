package com.example.pets.controller;

import com.example.pets.dto.response.PetResponse;
import com.example.pets.service.PetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping({"/pet"})
@RequiredArgsConstructor
public class PetController {

    private final PetService petService;
    @GetMapping({"/{id}"})
    public ResponseEntity<PetResponse> getPet(@PathVariable("id") Long id){
        var result = this.petService.findById(id);
        return ResponseEntity.ok(null);
    }

}
