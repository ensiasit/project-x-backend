package com.ensiasit.projectx.services;

import com.ensiasit.projectx.dto.MemberRequest;
import com.ensiasit.projectx.dto.MemberResponse;
import com.ensiasit.projectx.dto.TeamRequest;
import com.ensiasit.projectx.dto.TeamResponse;

import java.util.List;

public interface TeamService {
    List<TeamResponse> getAll();

    TeamResponse createTeam(String userEmail, TeamRequest team);

    TeamResponse getTeam(long id);

    TeamResponse deleteTeam(String userEmail, long id);

    TeamResponse updateTeam(String userEmail, long id, TeamRequest payload);

    TeamResponse addMember(String userEmail, long id, MemberRequest member);
}
