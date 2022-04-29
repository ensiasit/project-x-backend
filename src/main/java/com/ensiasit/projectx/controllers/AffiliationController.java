package com.ensiasit.projectx.controllers;


import com.ensiasit.projectx.dto.AffiliationRequestDto;
import com.ensiasit.projectx.dto.AffiliationResponseDto;
import com.ensiasit.projectx.services.AffiliationService;
import com.ensiasit.projectx.utils.Constants;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Api(tags = "/affiliations")
@CrossOrigin(origins = "*")
@RestController
@RequestMapping(Constants.API_PREFIX + "/affiliations")
@RequiredArgsConstructor
public class AffiliationController {
    private final AffiliationService affiliationService;

    @ApiOperation("Returns all affiliations")
    @GetMapping
    public List<AffiliationResponseDto> getAll() {
        return affiliationService.getAll();
    }

    @ApiOperation("Returns an a affiliation")
    @GetMapping("/{id}")
    public AffiliationResponseDto getOne(@PathVariable long id) {
        return affiliationService.getOne(id);
    }

    @ApiOperation("Creates an affiliation")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public AffiliationResponseDto addOne(@Valid @ModelAttribute AffiliationRequestDto affiliation) {
        return affiliationService.addOne(affiliation);
    }

    @ApiOperation("Deletes an affiliation")
    @DeleteMapping("/{id}")
    public AffiliationResponseDto deleteOne(@PathVariable long id) {
        return affiliationService.deleteOne(id);
    }

    @ApiOperation("Updates an affiliation")
    @PutMapping(path = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public AffiliationResponseDto updateOne(@PathVariable long id, @Valid @ModelAttribute AffiliationRequestDto payload) {
        return affiliationService.updateOne(id, payload);
    }
}
