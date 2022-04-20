package com.ensiasit.projectx.services;

import com.ensiasit.projectx.dto.TeamDto;
import com.ensiasit.projectx.exceptions.BadRequestException;
import com.ensiasit.projectx.exceptions.ForbiddenException;
import com.ensiasit.projectx.exceptions.NotFoundException;
import com.ensiasit.projectx.mappers.TeamMapper;
import com.ensiasit.projectx.models.Affiliation;
import com.ensiasit.projectx.models.Team;
import com.ensiasit.projectx.models.UserContestRole;
import com.ensiasit.projectx.repositories.AffiliationRepository;
import com.ensiasit.projectx.repositories.TeamRepository;
import com.ensiasit.projectx.repositories.UserContestRoleRepository;
import com.ensiasit.projectx.utils.RoleEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TeamServiceImpl implements TeamService {
    private final TeamRepository teamRepository;
    private final UserContestRoleRepository userContestRoleRepository;
    private final AffiliationRepository affiliationRepository;
    private final AdminService adminService;
    private final TeamMapper teamMapper;

    @Override
    public List<TeamDto> getAll() {
        return teamRepository.findAll().stream()
                .map(teamMapper::toTeamDto)
                .toList();
    }

    @Override
    public TeamDto createTeam(String userEmail, TeamDto teamDto) {
        if (!adminService.isAdmin(userEmail)) {
            throw new ForbiddenException("User has no write access.");
        }

        Team team = teamRepository.save(teamMapper.fromTeamDto(teamDto));

        return teamMapper.toTeamDto(team);
    }

    @Override
    public TeamDto getTeam(long id) {
        Team team = extractTeam(id);

        return teamMapper.toTeamDto(team);
    }

    @Override
    public TeamDto deleteTeam(String userEmail, long id) {
        if (userHasNoWriteAccess(userEmail, id)) {
            throw new ForbiddenException("User has no write access.");
        }

        Team team = extractTeam(id);
        teamRepository.deleteById(id);

        return teamMapper.toTeamDto(team);
    }

    @Override
    public TeamDto updateTeam(String userEmail, long id, TeamDto payload) {
        if (userHasNoWriteAccess(userEmail, id)) {
            throw new ForbiddenException("User has no write access.");
        }

        Team team = extractTeam(id);

        Optional<Affiliation> optionalAffiliation = affiliationRepository.findById(payload.getAffiliationId());

        if (optionalAffiliation.isEmpty()) {
            throw new BadRequestException("Incorrect affiliation id");
        }

        Affiliation affiliation = optionalAffiliation.get();

        team.setName(payload.getName());
        team.setAffiliation(affiliation);
        teamRepository.save(team);

        return teamMapper.toTeamDto(team);
    }

    private Team extractTeam(long id) {
        Optional<Team> teamOptional = teamRepository.findById(id);

        if (teamOptional.isEmpty()) {
            throw new NotFoundException("Incorrect team id");
        }

        return teamOptional.get();
    }

    private boolean userHasNoWriteAccess(String userEmail, long contestId) {
        if (adminService.isAdmin(userEmail)) {
            return false;
        }

        List<UserContestRole> userContestRoles = userContestRoleRepository.findAllByUserEmail(userEmail);

        return userContestRoles.stream()
                .filter(userContestRole -> userContestRole.getContest().getId() == contestId)
                .noneMatch(userContestRole ->
                        userContestRole.getRole().equals(RoleEnum.ROLE_ADMIN) || userContestRole.getRole().equals(RoleEnum.ROLE_MODERATOR));
    }
}
