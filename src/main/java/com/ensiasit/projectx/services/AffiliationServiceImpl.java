package com.ensiasit.projectx.services;

import com.ensiasit.projectx.annotations.SecureAdmin;
import com.ensiasit.projectx.dto.AffiliationRequest;
import com.ensiasit.projectx.dto.AffiliationResponse;
import com.ensiasit.projectx.exceptions.ServerErrorException;
import com.ensiasit.projectx.mappers.AffiliationMapper;
import com.ensiasit.projectx.models.Affiliation;
import com.ensiasit.projectx.repositories.AffiliationRepository;
import com.ensiasit.projectx.repositories.TeamRepository;
import com.ensiasit.projectx.utils.Helpers;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AffiliationServiceImpl implements AffiliationService {
    private final AffiliationRepository affiliationRepository;
    private final AffiliationMapper affiliationMapper;
    private final TeamRepository teamRepository;
    private final Helpers helpers;

    @Override
    public List<AffiliationResponse> getAll() {
        return affiliationRepository.findAll().stream()
                .map(affiliationMapper::toDto)
                .toList();
    }

    @SecureAdmin
    @Override
    public AffiliationResponse createAffiliation(String userEmail, AffiliationRequest affiliationDto) {
        Affiliation affiliation = affiliationRepository.save(affiliationMapper.fromDto(affiliationDto));

        return affiliationMapper.toDto(affiliation);
    }

    @Override
    public AffiliationResponse getAffiliation(long id) {
        Affiliation affiliation = helpers.extractById(id, affiliationRepository);

        return affiliationMapper.toDto(affiliation);
    }

    @SecureAdmin
    @Override
    public AffiliationResponse deleteAffiliation(String userEmail, long id) {
        Affiliation affiliation = helpers.extractById(id, affiliationRepository);

        teamRepository.findAll().stream().filter(team -> team.getAffiliation().equals(affiliation))
                .forEach(team -> team.setAffiliation(null));
        affiliationRepository.deleteById(id);

        return affiliationMapper.toDto(affiliation);
    }

    @SecureAdmin
    @Override
    public AffiliationResponse updateAffiliation(String userEmail, long id, AffiliationRequest payload) {
        Affiliation affiliation = helpers.extractById(id, affiliationRepository);

        affiliation.setName(payload.getName());
        affiliation.setCountry(payload.getCountry());

        if (payload.getLogo() != null) {
            try {
                affiliation.setLogo(payload.getLogo().getBytes());
            } catch (IOException e) {
                throw new ServerErrorException("Could not read file content.");
            }
        }

        affiliationRepository.save(affiliation);

        return affiliationMapper.toDto(affiliation);
    }
}
