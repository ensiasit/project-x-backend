package com.ensiasit.projectx.services;

import com.ensiasit.projectx.dto.ContestDto;
import com.ensiasit.projectx.dto.UserContestRoleDto;

import java.util.List;

public interface ContestService {
    List<ContestDto> getAll();

    ContestDto createContest(ContestDto contest);

    ContestDto getContest(long id);

    ContestDto deleteContest(long id);

    List<UserContestRoleDto> getAllUserContests(String userEmail);

    ContestDto updateContest(long id, ContestDto payload);
}
