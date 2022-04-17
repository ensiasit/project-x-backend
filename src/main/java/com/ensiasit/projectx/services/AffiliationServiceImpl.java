package com.ensiasit.projectx.services;

import com.ensiasit.projectx.dto.AffiliationDto;
import com.ensiasit.projectx.exceptions.NotFoundException;
import com.ensiasit.projectx.models.Affiliation;
import com.ensiasit.projectx.repositories.AffiliationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AffiliationServiceImpl implements AffiliationService {

    private final AffiliationRepository affiliationRepository;

    @Override
    public List<AffiliationDto> getAll() {

        return affiliationRepository.findAll().stream()
                .map(affiliation -> AffiliationDto.builder()
                        .id(affiliation.getId())
                        .name(affiliation.getName())
                        .country(affiliation.getCountry())
                        .logo(affiliation.getLogo())
                        .build())
                .toList();
    }

    @Override
    public AffiliationDto createAffiliation(AffiliationDto affiliationDto) {
        Affiliation affiliation = affiliationRepository.save(Affiliation.builder()
                .name(affiliationDto.getName())
                .country(affiliationDto.getCountry())
                .logo(affiliationDto.getLogo())
                .build());

        return AffiliationDto.builder()
                .id(affiliation.getId())
                .name(affiliation.getName())
                .country(affiliation.getCountry())
                .logo(affiliation.getLogo())
                .build();
    }

    @Override
    public AffiliationDto getAffiliation(long id) {
        Affiliation affiliation = extractAffiliation(id);

        return AffiliationDto.builder()
                .id(affiliation.getId())
                .name(affiliation.getName())
                .country(affiliation.getCountry())
                .logo(affiliation.getLogo())
                .build();
    }

    @Override
    public AffiliationDto deleteAffiliation(long id) {
        Affiliation affiliation = extractAffiliation(id);
        affiliationRepository.deleteById(id);

        return AffiliationDto.builder()
                .id(affiliation.getId())
                .name(affiliation.getName())
                .country(affiliation.getCountry())
                .logo(affiliation.getLogo())
                .build();
    }

    @Override
    public AffiliationDto updateAffiliation(long id, AffiliationDto payload) {
        Affiliation affiliation = extractAffiliation(id);

        affiliation.setName(payload.getName());
        affiliation.setCountry(payload.getCountry());
        affiliation.setLogo(payload.getLogo());
        affiliationRepository.save(affiliation);

        return AffiliationDto.builder()
                .id(affiliation.getId())
                .name(affiliation.getName())
                .country(affiliation.getCountry())
                .logo(affiliation.getLogo())
                .build();
    }

    private Affiliation extractAffiliation(long id) {
        Optional<Affiliation> affiliationOptional = affiliationRepository.findById(id);

        if (affiliationOptional.isEmpty()) {
            throw new NotFoundException("Incorrect affiliation id");
        }

        return affiliationOptional.get();
    }
}
