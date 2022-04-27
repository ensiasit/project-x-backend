package com.ensiasit.projectx.services;

import com.ensiasit.projectx.dto.AffiliationRequestDto;
import com.ensiasit.projectx.dto.AffiliationResponseDto;
import com.ensiasit.projectx.mappers.AffiliationMapper;
import com.ensiasit.projectx.models.Affiliation;
import com.ensiasit.projectx.repositories.AffiliationRepository;
import com.ensiasit.projectx.utils.Helpers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AffiliationServiceImplTest {
    @Mock
    private AffiliationRepository affiliationRepository;

    @Mock
    private AffiliationMapper affiliationMapper;

    @Mock
    private Helpers helpers;

    @InjectMocks
    private AffiliationServiceImpl affiliationService;

    @Test
    void should_return_affiliations_dto_when_getAll() {
        Affiliation affiliation = mock(Affiliation.class);
        AffiliationResponseDto affiliationResponse = mock(AffiliationResponseDto.class);

        when(affiliationRepository.findAll()).thenReturn(List.of(affiliation));
        when(affiliationMapper.toDto(affiliation)).thenReturn(affiliationResponse);

        List<AffiliationResponseDto> responses = affiliationService.getAll();

        assertThat(responses).hasSize(1);
        assertThat(responses.get(0)).isEqualTo(affiliationResponse);
    }

    @Test
    void given_affiliation_id_should_return_dto_when_getOne() {
        Affiliation affiliation = Affiliation.builder().id(1L).build();
        AffiliationResponseDto dto = mock(AffiliationResponseDto.class);

        when(helpers.extractById(affiliation.getId(), affiliationRepository)).thenReturn(affiliation);
        when(affiliationMapper.toDto(affiliation)).thenReturn(dto);

        AffiliationResponseDto responseDto = affiliationService.getOne(affiliation.getId());

        assertThat(responseDto).isEqualTo(dto);
    }

    @Test
    void given_affiliation_request_should_return_dto_when_addOne() {
        AffiliationRequestDto requestDto = mock(AffiliationRequestDto.class);
        AffiliationResponseDto responseDto = mock(AffiliationResponseDto.class);
        Affiliation affiliation = mock(Affiliation.class);

        when(affiliationMapper.fromDto(requestDto)).thenReturn(affiliation);
        when(affiliationRepository.save(affiliation)).thenReturn(affiliation);
        when(affiliationMapper.toDto(affiliation)).thenReturn(responseDto);

        AffiliationResponseDto dto = affiliationService.addOne(requestDto);

        assertThat(dto).isEqualTo(responseDto);
    }

    @Test
    void given_id_should_delete_and_return_dto_when_deleteOne() {
        Affiliation affiliation = Affiliation.builder().id(1L).build();
        AffiliationResponseDto dto = mock(AffiliationResponseDto.class);

        when(helpers.extractById(affiliation.getId(), affiliationRepository)).thenReturn(affiliation);
        when(affiliationMapper.toDto(affiliation)).thenReturn(dto);

        AffiliationResponseDto responseDto = affiliationService.deleteOne(affiliation.getId());

        verify(affiliationRepository).deleteById(affiliation.getId());

        assertThat(responseDto).isEqualTo(dto);
    }

    @Test
    void given_id_and_affiliation_with_null_logo_should_update_and_return_dto_when_updateOne() {
        Affiliation affiliation = Affiliation.builder().id(1L).build();
        AffiliationRequestDto requestDto = AffiliationRequestDto.builder()
                .name("name")
                .country("country")
                .build();
        AffiliationResponseDto responseDto = mock(AffiliationResponseDto.class);

        when(helpers.extractById(affiliation.getId(), affiliationRepository)).thenReturn(affiliation);
        when(affiliationRepository.save(affiliation)).thenReturn(affiliation);
        when(affiliationMapper.toDto(affiliation)).thenReturn(responseDto);

        affiliationService.updateOne(affiliation.getId(), requestDto);

        ArgumentCaptor<Affiliation> affiliationCaptor = ArgumentCaptor.forClass(Affiliation.class);

        verify(affiliationRepository).save(affiliationCaptor.capture());

        assertThat(affiliationCaptor.getValue().getName()).isEqualTo(requestDto.getName());
        assertThat(affiliationCaptor.getValue().getCountry()).isEqualTo(requestDto.getCountry());
        assertThat(affiliationCaptor.getValue().getLogo()).isNull();
    }

    @Test
    void given_id_and_affiliation_with_not_null_logo_should_update_and_return_dto_when_updateOne() throws IOException {
        Affiliation affiliation = Affiliation.builder().id(1L).build();
        MultipartFile logo = mock(MultipartFile.class);
        byte[] logoBytes = new byte[0];
        AffiliationRequestDto requestDto = AffiliationRequestDto.builder()
                .name("name")
                .country("country")
                .logo(logo)
                .build();
        AffiliationResponseDto responseDto = mock(AffiliationResponseDto.class);

        when(helpers.extractById(affiliation.getId(), affiliationRepository)).thenReturn(affiliation);
        when(logo.getBytes()).thenReturn(logoBytes);
        when(affiliationRepository.save(affiliation)).thenReturn(affiliation);
        when(affiliationMapper.toDto(affiliation)).thenReturn(responseDto);

        affiliationService.updateOne(affiliation.getId(), requestDto);

        ArgumentCaptor<Affiliation> affiliationCaptor = ArgumentCaptor.forClass(Affiliation.class);

        verify(affiliationRepository).save(affiliationCaptor.capture());

        assertThat(affiliationCaptor.getValue().getName()).isEqualTo(requestDto.getName());
        assertThat(affiliationCaptor.getValue().getCountry()).isEqualTo(requestDto.getCountry());
        assertThat(affiliationCaptor.getValue().getLogo()).isEqualTo(logoBytes);
    }
}