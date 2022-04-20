package com.ensiasit.projectx.services;

import com.ensiasit.projectx.dto.TeamDto;

import java.util.List;

public interface TeamService {
    List<TeamDto> getAll();

    TeamDto createTeam(String userEmail, TeamDto team);

    TeamDto getTeam(long id);

    TeamDto deleteTeam(String userEmail, long id);

    TeamDto updateTeam(String userEmail, long id, TeamDto payload);
}
