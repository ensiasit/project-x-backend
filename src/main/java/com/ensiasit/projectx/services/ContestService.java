package com.ensiasit.projectx.services;

import com.ensiasit.projectx.models.Contest;

import java.util.List;
import java.util.Optional;

public interface ContestService {
    List<Contest> getAll();

    Contest createContest(Contest payload);

    Optional<Contest> getContest(Long id);

    void deleteContest(Long id);
}
