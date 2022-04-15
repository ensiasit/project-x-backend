package com.ensiasit.projectx.services;

import com.ensiasit.projectx.dto.ContestDto;
import com.ensiasit.projectx.models.Contest;

import java.util.List;
import java.util.Optional;

public interface ContestService {
    List<Contest> getAll();

    ContestDto createContest(ContestDto contest);

    Optional<ContestDto> getContest(Long id);

    void deleteContest(Long id);
}
