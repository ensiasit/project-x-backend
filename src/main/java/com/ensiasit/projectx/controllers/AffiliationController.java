package com.ensiasit.projectx.controllers;


import com.ensiasit.projectx.dto.AffiliationDto;
import com.ensiasit.projectx.services.AffiliationService;
import com.ensiasit.projectx.utils.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(Constants.API_PREFIX + "/affiliations")
@RequiredArgsConstructor
public class AffiliationController {

    private final AffiliationService affiliationService;

    @PostMapping
    private AffiliationDto createAffiliation(@Valid @RequestBody AffiliationDto affiliation) {
        return affiliationService.createAffiliation(affiliation);
    }

    @GetMapping
    private List<AffiliationDto> getAll() {
        return affiliationService.getAll();
    }

    @GetMapping("/{id}")
    private AffiliationDto getAffiliation(@PathVariable long id) {
        return affiliationService
                .getAffiliation(id);
    }

    @DeleteMapping("/{id}")
    private AffiliationDto deleteAffiliation(@PathVariable long id) {
        return affiliationService.deleteAffiliation(id);
    }

    @PutMapping("/{id}")
    private AffiliationDto updateAffiliation(@PathVariable Long id, @Valid @RequestBody AffiliationDto payload) {
        return affiliationService.updateAffiliation(id, payload);
    }
}
