package com.ensiasit.projectx.services;

import com.ensiasit.projectx.dto.AffiliationRequestDto;
import com.ensiasit.projectx.dto.AffiliationResponseDto;

import java.util.List;

public interface AffiliationService {
    List<AffiliationResponseDto> getAll();

    AffiliationResponseDto getOne(long id);

    AffiliationResponseDto addOne(AffiliationRequestDto affiliation);

    AffiliationResponseDto deleteOne(long id);

    AffiliationResponseDto updateOne(long id, AffiliationRequestDto payload);
}
