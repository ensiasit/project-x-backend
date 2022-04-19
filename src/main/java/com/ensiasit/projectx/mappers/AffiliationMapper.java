package com.ensiasit.projectx.mappers;

import com.ensiasit.projectx.dto.AffiliationRequest;
import com.ensiasit.projectx.dto.AffiliationResponse;
import com.ensiasit.projectx.exceptions.ServerErrorException;
import com.ensiasit.projectx.models.Affiliation;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class AffiliationMapper {
    public AffiliationResponse toDto(Affiliation affiliation) {
        return AffiliationResponse.builder()
                .id(affiliation.getId())
                .name(affiliation.getName())
                .country(affiliation.getCountry())
                .logo(affiliation.getLogo())
                .build();
    }

    public Affiliation fromDto(AffiliationRequest affiliationDto) {
        try {
            return Affiliation.builder()
                    .id(affiliationDto.getId())
                    .name(affiliationDto.getName())
                    .country(affiliationDto.getCountry())
                    .logo(affiliationDto.getLogo() != null ? affiliationDto.getLogo().getBytes() : null)
                    .build();
        } catch (IOException e) {
            throw new ServerErrorException("Could not read file content");
        }
    }
}
