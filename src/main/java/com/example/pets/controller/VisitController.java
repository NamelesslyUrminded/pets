package com.example.pets.controller;

import com.example.pets.dto.request.VisitRequest;
import com.example.pets.entity.Visit;
import com.example.pets.service.OwnerService;
import com.example.pets.service.VisitService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@CrossOrigin(origins = "*")
@RestController
@RequestMapping({"/visit"})
@RequiredArgsConstructor
public class VisitController {

    private final VisitService visitService;

    @PostMapping
    public ResponseEntity<Visit> logVisit(@RequestBody VisitRequest visitRequest) {
        return ResponseEntity.ok(visitService.save(visitRequest));
    }
    @GetMapping({"/{id}"})
    public ResponseEntity<Visit> getVisit(@PathVariable Long id){
        return ResponseEntity.ok(visitService.findById(id));
    }

    @DeleteMapping({"/{id}"})
    public ResponseEntity<?> deleteVisit(@PathVariable Long id) {
        visitService.delete(id);
        return ResponseEntity.ok().build();
    }

}
