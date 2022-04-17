package com.ensiasit.projectx.controllers;


import com.ensiasit.projectx.exceptions.AffiliationNotFoundException;
import com.ensiasit.projectx.models.Affiliation;
import com.ensiasit.projectx.services.AffiliationService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@RequestMapping("/affiliation")
@RestController
public class AffiliationController {

    private final AffiliationService affiliationService;

    @GetMapping
    private List<Affiliation> getAll(){
        return affiliationService.getAll();
    }

    @GetMapping("/{id}")
    private Affiliation get(@PathVariable Long id){
        return affiliationService
                .get(id)
                .orElseThrow(()-> new AffiliationNotFoundException(String.format("Affiliation with id : %s not found",id)));
    }

    @PostMapping
    private Affiliation add(@RequestBody Affiliation affiliation){
        return affiliationService.add(affiliation);
    }

    @PutMapping("/{id}")
    private Affiliation update(@PathVariable Long id,@RequestBody Affiliation payload){
        return affiliationService
                .update(id,payload)
                .orElseThrow(()-> new AffiliationNotFoundException(String.format("Affiliation with id : %s not found",id)));
    }

    @DeleteMapping("/{id}")
    private String delete(@PathVariable Long id){
        affiliationService.delete(id);
        return String.format("Affiliation with id : %s deleted successfuly",id);
    }


}
