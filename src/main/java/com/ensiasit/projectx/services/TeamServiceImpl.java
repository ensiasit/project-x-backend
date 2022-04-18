package com.ensiasit.projectx.services;

import com.ensiasit.projectx.dto.TeamDto;
import com.ensiasit.projectx.exceptions.BadRequestException;
import com.ensiasit.projectx.exceptions.NotFoundException;
import com.ensiasit.projectx.models.Affiliation;
import com.ensiasit.projectx.models.Contest;
import com.ensiasit.projectx.models.Team;
import com.ensiasit.projectx.repositories.AffiliationRepository;
import com.ensiasit.projectx.repositories.ContestRepository;
import com.ensiasit.projectx.repositories.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TeamServiceImpl implements TeamService {
    private final TeamRepository teamRepository;
    private final ContestRepository contestRepository;
    private final AffiliationRepository affiliationRepository;

    @Override
    public List<TeamDto> getAll() {
        return teamRepository.findAll().stream()
                .map(team -> TeamDto.builder()
                        .id(team.getId())
                        .name(team.getName())
                        .contestId(team.getContest().getId())
                        .affiliationId(team.getAffiliation().getId())
                        .affiliationName(team.getAffiliation().getName())
                        .build())
                .toList();
    }

    @Override
    public TeamDto createTeam(TeamDto teamDto) {
        if (teamRepository.existsByName(teamDto.getName())) {
            throw new BadRequestException("Name already taken");
        }

        Optional<Contest> optionalContest = contestRepository.findById(teamDto.getContestId());

        if (optionalContest.isEmpty()) {
            throw new BadRequestException("Incorrect contest id");
        }

        Optional<Affiliation> optionalAffiliation = affiliationRepository.findById(teamDto.getAffiliationId());

        if (optionalAffiliation.isEmpty()) {
            throw new BadRequestException("Incorrect affiliation id");
        }

        Contest contest = optionalContest.get();
        Affiliation affiliation = optionalAffiliation.get();

        Team team = teamRepository.save(Team.builder()
                .name(teamDto.getName())
                .contest(contest)
                .affiliation(affiliation)
                .build());

        return TeamDto.builder()
                .id(team.getId())
                .name(team.getName())
                .contestId(contest.getId())
                .affiliationId(affiliation.getId())
                .affiliationName(affiliation.getName())
                .build();
    }

    @Override
    public TeamDto getTeam(long id) {
        Team team = extractTeam(id);

        return TeamDto.builder()
                .id(team.getId())
                .name(team.getName())
                .contestId(team.getContest().getId())
                .affiliationId(team.getAffiliation().getId())
                .affiliationName(team.getAffiliation().getName())
                .build();
    }

    @Override
    public TeamDto deleteTeam(long id) {
        Team team = extractTeam(id);
        teamRepository.deleteById(id);

        return TeamDto.builder()
                .id(team.getId())
                .name(team.getName())
                .contestId(team.getContest().getId())
                .affiliationId(team.getAffiliation().getId())
                .affiliationName(team.getAffiliation().getName())
                .build();
    }

    @Override
    public TeamDto updateTeam(long id, TeamDto payload) {
        Team team = extractTeam(id);

        if (teamRepository.existsByName(payload.getName()) && !team.getName().equals(payload.getName())) {
            throw new BadRequestException("Name already taken");
        }

        Optional<Affiliation> optionalAffiliation = affiliationRepository.findById(payload.getAffiliationId());

        if (optionalAffiliation.isEmpty()) {
            throw new BadRequestException("Incorrect affiliation id");
        }

        Affiliation affiliation = optionalAffiliation.get();

        team.setName(payload.getName());
        team.setAffiliation(affiliation);
        teamRepository.save(team);

        return TeamDto.builder()
                .id(team.getId())
                .name(team.getName())
                .contestId(team.getContest().getId())
                .affiliationId(team.getAffiliation().getId())
                .affiliationName(team.getAffiliation().getName())
                .build();
    }

    private Team extractTeam(long id) {
        Optional<Team> teamOptional = teamRepository.findById(id);

        if (teamOptional.isEmpty()) {
            throw new NotFoundException("Incorrect team id");
        }

        return teamOptional.get();
    }
}
