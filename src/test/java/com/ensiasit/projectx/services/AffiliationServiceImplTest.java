package com.ensiasit.projectx.services;

import com.ensiasit.projectx.dto.AffiliationResponseDto;
import com.ensiasit.projectx.mappers.AffiliationMapper;
import com.ensiasit.projectx.models.Affiliation;
import com.ensiasit.projectx.repositories.AffiliationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AffiliationServiceImplTest {
    @Mock
    private AffiliationRepository affiliationRepository;

    @Mock
    private AffiliationMapper affiliationMapper;

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
    void given_not_admin_should_throw_exception_when_createAffiliation() {

    }
}