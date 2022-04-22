package com.ensiasit.projectx.services;

import com.ensiasit.projectx.dto.ContestDto;
import com.ensiasit.projectx.dto.UserContestRoleDto;
import com.ensiasit.projectx.utils.RoleEnum;

import java.util.List;

public interface ContestService {
    List<ContestDto> getAll();

    ContestDto createContest(String userEmail, ContestDto contest);

    ContestDto getContest(long id);

    ContestDto deleteContest(String userEmail, long id);

    List<UserContestRoleDto> getAllUserContests(String userEmail);

    ContestDto updateContest(String userEmail, long id, ContestDto payload);

    List<UserContestRoleDto> getUserContestRoles(long id);

    UserContestRoleDto updateUserContestRole(String principalEmail, long contestId, long userId, RoleEnum role);
}
