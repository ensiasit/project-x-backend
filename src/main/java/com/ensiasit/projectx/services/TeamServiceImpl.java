package com.ensiasit.projectx.services;

import com.ensiasit.projectx.dto.MemberRequest;
import com.ensiasit.projectx.dto.MemberResponse;
import com.ensiasit.projectx.dto.TeamRequest;
import com.ensiasit.projectx.dto.TeamResponse;
import com.ensiasit.projectx.exceptions.BadRequestException;
import com.ensiasit.projectx.exceptions.ForbiddenException;
import com.ensiasit.projectx.mappers.TeamMapper;
import com.ensiasit.projectx.models.Affiliation;
import com.ensiasit.projectx.models.Team;
import com.ensiasit.projectx.models.User;
import com.ensiasit.projectx.models.UserContestRole;
import com.ensiasit.projectx.repositories.AffiliationRepository;
import com.ensiasit.projectx.repositories.TeamRepository;
import com.ensiasit.projectx.repositories.UserContestRoleRepository;
import com.ensiasit.projectx.repositories.UserRepository;
import com.ensiasit.projectx.utils.Helpers;
import com.ensiasit.projectx.utils.RoleEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TeamServiceImpl implements TeamService {
    private final TeamRepository teamRepository;
    private final AffiliationRepository affiliationRepository;

    private final UserContestRoleRepository userContestRoleRepository;
    private final UserRepository userRepository;
    private final ContestService contestService;
    private final TeamMapper teamMapper;
    private final Helpers helpers;

    @Override
    public List<TeamResponse> getAll() {
        return teamRepository.findAll().stream()
                .map(teamMapper::toTeamDto)
                .toList();
    }

    @Override
    public TeamResponse createTeam(String userEmail, TeamRequest teamDto) {
        if (contestService.userHasNoWriteAccess(userEmail, teamDto.getContestId())) {
            throw new ForbiddenException("User has no write access.");
        }
        Team team = teamRepository.save(teamMapper.fromTeamDto(teamDto));

        return teamMapper.toTeamDto(team);
    }

    @Override
    public TeamResponse getTeam(long id) {
        Team team = helpers.extractById(id, teamRepository);

        return teamMapper.toTeamDto(team);
    }

    @Override
    public TeamResponse deleteTeam(String userEmail, long id) {
        Team team = helpers.extractById(id, teamRepository);

        if (contestService.userHasNoWriteAccess(userEmail, team.getContest().getId())) {
            throw new ForbiddenException("User has no write access.");
        }
        teamRepository.deleteById(id);

        return teamMapper.toTeamDto(team);
    }

    @Override
    public TeamResponse updateTeam(String userEmail, long id, TeamRequest payload) {
        if (contestService.userHasNoWriteAccess(userEmail, payload.getContestId())) {
            throw new ForbiddenException("User has no write access.");
        }

        Team team = helpers.extractById(id, teamRepository);
        Affiliation affiliation = helpers.extractById(payload.getAffiliationId(), affiliationRepository);

        team.setName(payload.getName());
        team.setAffiliation(affiliation);
        teamRepository.save(team);

        return teamMapper.toTeamDto(team);
    }

    @Override
    public TeamResponse addMember(String userEmail, long id, MemberRequest member) {
        Team team = helpers.extractById(id, teamRepository);

        if (contestService.userHasNoWriteAccess(userEmail, team.getContest().getId())) {
            throw new ForbiddenException("User has no write access.");
        }

        Optional<User> optionalUser = userRepository.findByEmail(member.getEmail());
        if (optionalUser.isEmpty()) {
            throw new BadRequestException("Incorrect member email.");
        }
        User user = optionalUser.get();

        team.getMembers().add(user);
        user.getTeams().add(team);

        teamRepository.save(team);
        userRepository.save(user);
        userContestRoleRepository.save(UserContestRole.builder()
                .user(user)
                .contest(team.getContest())
                .role(RoleEnum.ROLE_USER)
                .build());

        return teamMapper.toTeamDto(team);
    }
}
