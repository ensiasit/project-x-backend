package com.ensiasit.projectx.controllers;


import com.ensiasit.projectx.dto.AffiliationRequest;
import com.ensiasit.projectx.dto.AffiliationResponse;
import com.ensiasit.projectx.services.AffiliationService;
import com.ensiasit.projectx.utils.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(Constants.API_PREFIX + "/affiliations")
@RequiredArgsConstructor
public class AffiliationController {
    private final AffiliationService affiliationService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public AffiliationResponse createAffiliation(Principal principal, @Valid @ModelAttribute AffiliationRequest affiliation) {
        return affiliationService.createAffiliation(principal.getName(), affiliation);
    }

    @GetMapping
    public List<AffiliationResponse> getAll() {
        return affiliationService.getAll();
    }

    @GetMapping("/{id}")
    public AffiliationResponse getAffiliation(@PathVariable long id) {
        return affiliationService.getAffiliation(id);
    }

    @DeleteMapping("/{id}")
    public AffiliationResponse deleteAffiliation(Principal principal, @PathVariable long id) {
        return affiliationService.deleteAffiliation(principal.getName(), id);
    }

    @PutMapping(path = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public AffiliationResponse updateAffiliation(Principal principal, @PathVariable Long id, @Valid @ModelAttribute AffiliationRequest payload) {
        return affiliationService.updateAffiliation(principal.getName(), id, payload);
    }
}
