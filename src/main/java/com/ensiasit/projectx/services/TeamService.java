package com.ensiasit.projectx.services;

import com.ensiasit.projectx.dto.TeamDto;

import java.util.List;

public interface TeamService {
    List<TeamDto> getAll();

    TeamDto createTeam(TeamDto team);

    TeamDto getTeam(long id);

    TeamDto deleteTeam(long id);

    TeamDto updateTeam(long id, TeamDto payload);
}
