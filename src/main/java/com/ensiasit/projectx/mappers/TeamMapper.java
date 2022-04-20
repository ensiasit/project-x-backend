package com.ensiasit.projectx.mappers;

import com.ensiasit.projectx.dto.TeamDto;
import com.ensiasit.projectx.exceptions.BadRequestException;
import com.ensiasit.projectx.models.Affiliation;
import com.ensiasit.projectx.models.Team;
import com.ensiasit.projectx.repositories.AffiliationRepository;
import com.ensiasit.projectx.repositories.ContestRepository;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
public class TeamMapper {
    private final ContestRepository contestRepository;
    private final AffiliationRepository affiliationRepository;

    public Team fromTeamDto(TeamDto teamDto) {
        Optional<Affiliation> optionalAffiliation = affiliationRepository.findById(teamDto.getAffiliationId());

        if (optionalAffiliation.isEmpty()) {
            throw new BadRequestException("Incorrect affiliation id");
        }

        Affiliation affiliation = optionalAffiliation.get();

        return Team.builder()
                .id(teamDto.getId())
                .name(teamDto.getName())
                .affiliation(affiliation)
                .build();
    }

    public TeamDto toTeamDto(Team team) {
        return TeamDto.builder()
                .id(team.getId())
                .name(team.getName())
                .affiliationId(team.getAffiliation().getId())
                .build();
    }
}
