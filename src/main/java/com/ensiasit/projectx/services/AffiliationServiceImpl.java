package com.ensiasit.projectx.services;

import com.ensiasit.projectx.dto.AffiliationRequest;
import com.ensiasit.projectx.dto.AffiliationResponse;
import com.ensiasit.projectx.exceptions.ForbiddenException;
import com.ensiasit.projectx.exceptions.NotFoundException;
import com.ensiasit.projectx.exceptions.ServerErrorException;
import com.ensiasit.projectx.mappers.AffiliationMapper;
import com.ensiasit.projectx.models.Affiliation;
import com.ensiasit.projectx.repositories.AffiliationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AffiliationServiceImpl implements AffiliationService {
    private final AffiliationRepository affiliationRepository;
    private final AffiliationMapper affiliationMapper;
    private final AdminService adminService;

    @Override
    public List<AffiliationResponse> getAll() {
        return affiliationRepository.findAll().stream()
                .map(affiliationMapper::toDto)
                .toList();
    }

    @Override
    public AffiliationResponse createAffiliation(String userEmail, AffiliationRequest affiliationDto) {
        if (!adminService.isAdmin(userEmail)) {
            throw new ForbiddenException("User is not admin");
        }

        Affiliation affiliation = affiliationRepository.save(affiliationMapper.fromDto(affiliationDto));

        return affiliationMapper.toDto(affiliation);
    }

    @Override
    public AffiliationResponse getAffiliation(long id) {
        Affiliation affiliation = extractAffiliation(id);

        return affiliationMapper.toDto(affiliation);
    }

    @Override
    public AffiliationResponse deleteAffiliation(String userEmail, long id) {
        if (!adminService.isAdmin(userEmail)) {
            throw new ForbiddenException("User is not admin");
        }

        Affiliation affiliation = extractAffiliation(id);

        affiliationRepository.deleteById(id);

        return affiliationMapper.toDto(affiliation);
    }

    @Override
    public AffiliationResponse updateAffiliation(String userEmail, long id, AffiliationRequest payload) {
        if (!adminService.isAdmin(userEmail)) {
            throw new ForbiddenException("User is not admin");
        }

        Affiliation affiliation = extractAffiliation(id);

        affiliation.setName(payload.getName());
        affiliation.setCountry(payload.getCountry());

        try {
            affiliation.setLogo(payload.getLogo().getBytes());
        } catch (IOException e) {
            throw new ServerErrorException("Could not read file content.");
        }

        affiliationRepository.save(affiliation);

        return affiliationMapper.toDto(affiliation);
    }

    private Affiliation extractAffiliation(long id) {
        Optional<Affiliation> affiliationOptional = affiliationRepository.findById(id);

        if (affiliationOptional.isEmpty()) {
            throw new NotFoundException("Incorrect affiliation id");
        }

        return affiliationOptional.get();
    }
}
