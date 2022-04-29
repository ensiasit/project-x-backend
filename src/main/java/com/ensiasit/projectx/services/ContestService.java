package com.ensiasit.projectx.services;

import com.ensiasit.projectx.dto.ContestDto;
import com.ensiasit.projectx.dto.UserContestRoleDto;
import com.ensiasit.projectx.utils.RoleEnum;

import java.util.List;

public interface ContestService {
    List<ContestDto> getAll();

    ContestDto getOne(long id);

    ContestDto addOne(ContestDto contest);

    ContestDto deleteOne(long id);

    ContestDto updateOne(long id, ContestDto payload);

    List<UserContestRoleDto> getAllRoles(long id);

    List<UserContestRoleDto> getAllRolesByUserEmail(String email);

    UserContestRoleDto updateRole(String principalEmail, long contestId, long userId, RoleEnum role);
}
