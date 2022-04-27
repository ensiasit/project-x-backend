package com.ensiasit.projectx.mappers;

import com.ensiasit.projectx.dto.AffiliationRequestDto;
import com.ensiasit.projectx.dto.AffiliationResponseDto;
import com.ensiasit.projectx.exceptions.ServerErrorException;
import com.ensiasit.projectx.models.Affiliation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AffiliationMapperTest {
    @InjectMocks
    private AffiliationMapper affiliationMapper;

    @Test
    void given_affiliation_should_return_dto_when_toDto() {
        Affiliation affiliation = Affiliation.builder()
                .id(1L)
                .name("name")
                .country("country")
                .logo(new byte[]{})
                .build();

        AffiliationResponseDto affiliationResponse = affiliationMapper.toDto(affiliation);

        assertThat(affiliationResponse.getId()).isEqualTo(affiliation.getId());
        assertThat(affiliationResponse.getName()).isEqualTo(affiliation.getName());
        assertThat(affiliationResponse.getCountry()).isEqualTo(affiliation.getCountry());
        assertThat(affiliationResponse.getLogo()).isEqualTo(affiliation.getLogo());
    }

    @Test
    void given_dto_with_non_empty_logo_should_return_affiliation_when_fromDto() throws IOException {
        byte[] logoBytes = new byte[]{};
        MultipartFile logo = mock(MultipartFile.class);
        AffiliationRequestDto affiliationRequest = AffiliationRequestDto.builder()
                .id(1L)
                .name("name")
                .country("country")
                .logo(logo)
                .build();

        when(logo.getBytes()).thenReturn(logoBytes);

        Affiliation affiliation = affiliationMapper.fromDto(affiliationRequest);

        assertThat(affiliation.getId()).isEqualTo(affiliationRequest.getId());
        assertThat(affiliation.getName()).isEqualTo(affiliationRequest.getName());
        assertThat(affiliation.getCountry()).isEqualTo(affiliationRequest.getCountry());
        assertThat(affiliation.getLogo()).isEqualTo(logoBytes);
    }

    @Test
    void given_dto_with_non_empty_invalid_logo_should_throw_exception_when_fromDto() throws IOException {
        MultipartFile logo = mock(MultipartFile.class);
        AffiliationRequestDto affiliationRequest = AffiliationRequestDto.builder()
                .id(1L)
                .name("name")
                .country("country")
                .logo(logo)
                .build();

        when(logo.getBytes()).thenThrow(IOException.class);

        assertThrows(
                ServerErrorException.class,
                () -> affiliationMapper.fromDto(affiliationRequest)
        );
    }

    @Test
    void given_dto_with_empty_logo_should_return_affiliation_when_fromDto() {
        AffiliationRequestDto affiliationRequest = AffiliationRequestDto.builder()
                .id(1L)
                .name("name")
                .country("country")
                .logo(null)
                .build();

        Affiliation affiliation = affiliationMapper.fromDto(affiliationRequest);

        assertThat(affiliation.getId()).isEqualTo(affiliationRequest.getId());
        assertThat(affiliation.getName()).isEqualTo(affiliationRequest.getName());
        assertThat(affiliation.getCountry()).isEqualTo(affiliationRequest.getCountry());
        assertThat(affiliation.getLogo()).isNull();
    }
}