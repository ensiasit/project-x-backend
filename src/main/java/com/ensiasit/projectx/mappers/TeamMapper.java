package com.ensiasit.projectx.mappers;

import com.ensiasit.projectx.dto.TeamRequest;
import com.ensiasit.projectx.dto.TeamResponse;
import com.ensiasit.projectx.models.Affiliation;
import com.ensiasit.projectx.models.Contest;
import com.ensiasit.projectx.models.Team;
import com.ensiasit.projectx.models.User;
import com.ensiasit.projectx.repositories.AffiliationRepository;
import com.ensiasit.projectx.repositories.ContestRepository;
import com.ensiasit.projectx.utils.Helpers;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class TeamMapper {
    private final AffiliationRepository affiliationRepository;
    private final ContestRepository contestRepository;
    private final ContestMapper contestMapper;
    private final Helpers helpers;

    public TeamResponse toTeamDto(Team team) {
        return TeamResponse.builder()
                .id(team.getId())
                .name(team.getName())
                .contest(contestMapper.toContestDto(team.getContest()))
                .affiliation(team.getAffiliation())
                .membersEmails(team.getMembers() != null ? team.getMembers().stream().map(User::getEmail).collect(Collectors.toSet()) : Collections.emptySet())
                .build();
    }

    public Team fromTeamDto(TeamRequest teamDto) {
        Contest contest = helpers.extractById(teamDto.getContestId(), contestRepository);
        Affiliation affiliation = teamDto.getAffiliationId() != null ? helpers.extractById(teamDto.getAffiliationId(), affiliationRepository) : null;

        return Team.builder()
                .id(teamDto.getId())
                .name(teamDto.getName())
                .contest(contest)
                .affiliation(affiliation)
                .build();
    }
}
