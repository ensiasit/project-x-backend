package com.ensiasit.projectx.services;

import com.ensiasit.projectx.annotations.SecureAdmin;
import com.ensiasit.projectx.dto.AffiliationRequestDto;
import com.ensiasit.projectx.dto.AffiliationResponseDto;
import com.ensiasit.projectx.exceptions.ServerErrorException;
import com.ensiasit.projectx.mappers.AffiliationMapper;
import com.ensiasit.projectx.models.Affiliation;
import com.ensiasit.projectx.repositories.AffiliationRepository;
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
    private final Helpers helpers;

    @Override
    public List<AffiliationResponseDto> getAll() {
        return affiliationRepository.findAll().stream()
                .map(affiliationMapper::toDto)
                .toList();
    }

    @Override
    public AffiliationResponseDto getOne(long id) {
        Affiliation affiliation = helpers.extractById(id, affiliationRepository);

        return affiliationMapper.toDto(affiliation);
    }

    @SecureAdmin
    @Override
    public AffiliationResponseDto addOne(AffiliationRequestDto affiliationDto) {
        Affiliation affiliation = affiliationRepository.save(affiliationMapper.fromDto(affiliationDto));

        return affiliationMapper.toDto(affiliation);
    }

    @SecureAdmin
    @Override
    public AffiliationResponseDto deleteOne(long id) {
        Affiliation affiliation = helpers.extractById(id, affiliationRepository);

        affiliationRepository.deleteById(id);

        return affiliationMapper.toDto(affiliation);
    }

    @SecureAdmin
    @Override
    public AffiliationResponseDto updateOne(long id, AffiliationRequestDto payload) {
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
