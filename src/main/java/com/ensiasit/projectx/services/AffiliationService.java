package com.ensiasit.projectx.services;

import com.ensiasit.projectx.dto.AffiliationDto;

import java.util.List;

public interface AffiliationService {
    List<AffiliationDto> getAll();

    AffiliationDto getAffiliation(long id);

    AffiliationDto createAffiliation(AffiliationDto affiliation);

    AffiliationDto deleteAffiliation(long id);

    AffiliationDto updateAffiliation(long id, AffiliationDto payload);
}
