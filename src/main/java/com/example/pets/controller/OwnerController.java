package com.example.pets.controller;

import com.example.pets.dto.filter.OwnerFilter;
import com.example.pets.dto.response.OwnerResponse;
import com.example.pets.entity.Owner;
import com.example.pets.service.ClientService;
import com.example.pets.service.OwnerService;
import com.example.pets.service.mapper.ResponseMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = {"*"})
@RestController
@RequestMapping({"/owner"})
@RequiredArgsConstructor
public class OwnerController {

    private final OwnerService ownerService;
    private final ClientService clientService;
    private final ResponseMapper<Owner, OwnerResponse> ownerResponseMapper;


    @GetMapping({"/{id}"})
    public ResponseEntity getPet(@PathVariable("id") Long id) {
        var result = this.clientService.findById(id);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/search")
    public ResponseEntity findWithFilter(
            @RequestParam Integer page,
            @RequestParam Integer size,
            @RequestBody OwnerFilter ownerFilter) {
        System.out.println(ownerFilter);
        System.out.println(PageRequest.of(page, size));
        Page<OwnerResponse> responsePage = ownerService.findWithFilter(ownerFilter, PageRequest.of(page, size));
        return ResponseEntity.ok(responsePage);
    }

    @DeleteMapping
    public ResponseEntity deleteByList(@RequestBody List<Long> idList) {
        ownerService.deleteSeveral(idList);
        return ResponseEntity.ok().build();
    }
}
