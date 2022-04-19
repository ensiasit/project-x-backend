package com.ensiasit.projectx.services;

import com.ensiasit.projectx.dto.AffiliationRequest;
import com.ensiasit.projectx.dto.AffiliationResponse;

import java.util.List;

public interface AffiliationService {
    List<AffiliationResponse> getAll();

    AffiliationResponse getAffiliation(long id);

    AffiliationResponse createAffiliation(String userEmail, AffiliationRequest affiliation);

    AffiliationResponse deleteAffiliation(String userEmail, long id);

    AffiliationResponse updateAffiliation(String userEmail, long id, AffiliationRequest payload);
}
