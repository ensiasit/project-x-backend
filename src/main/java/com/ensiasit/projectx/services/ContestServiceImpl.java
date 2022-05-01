package com.ensiasit.projectx.services;

import com.ensiasit.projectx.annotations.SecureAdmin;
import com.ensiasit.projectx.dto.ContestDto;
import com.ensiasit.projectx.dto.TeamResponse;
import com.ensiasit.projectx.dto.UserContestRoleDto;
import com.ensiasit.projectx.exceptions.BadRequestException;
import com.ensiasit.projectx.exceptions.ForbiddenException;
import com.ensiasit.projectx.exceptions.NotFoundException;
import com.ensiasit.projectx.mappers.ContestMapper;
import com.ensiasit.projectx.mappers.TeamMapper;
import com.ensiasit.projectx.mappers.UserMapper;
import com.ensiasit.projectx.models.Contest;
import com.ensiasit.projectx.models.User;
import com.ensiasit.projectx.models.UserContestRole;
import com.ensiasit.projectx.repositories.ContestRepository;
import com.ensiasit.projectx.repositories.TeamRepository;
import com.ensiasit.projectx.repositories.UserContestRoleRepository;
import com.ensiasit.projectx.repositories.UserRepository;
import com.ensiasit.projectx.utils.Helpers;
import com.ensiasit.projectx.utils.RoleEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ContestServiceImpl implements ContestService {
    private final ContestRepository contestRepository;
    private final TeamRepository teamRepository;
    private final UserContestRoleRepository userContestRoleRepository;
    private final AdminService adminService;
    private final ContestMapper contestMapper;
    private final TeamMapper teamMapper;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final Helpers helpers;

    @Override
    public List<ContestDto> getAll() {
        return contestRepository.findAll().stream()
                .map(contestMapper::toContestDto)
                .toList();
    }

    @SecureAdmin
    @Override
    public ContestDto createContest(String userEmail, ContestDto contestDto) {
        Contest contest = contestRepository.save(contestMapper.fromContestDto(contestDto));

        return contestMapper.toContestDto(contest);
    }

    @Override
    public ContestDto getContest(long id) {
        Contest contest = helpers.extractById(id, contestRepository);

        return contestMapper.toContestDto(contest);
    }

    @SecureAdmin
    @Transactional
    @Override
    public ContestDto deleteContest(String userEmail, long id) {
        Contest contest = helpers.extractById(id, contestRepository);

        userContestRoleRepository.deleteAllByContestId(id);
        teamRepository.deleteAllByContestId(id);
        contestRepository.deleteById(id);

        return contestMapper.toContestDto(contest);
    }

    @Override
    public List<UserContestRoleDto> getAllUserContests(String userEmail) {
        if (adminService.isAdmin(userEmail)) {
            return getAll().stream()
                    .map(contestDto -> contestMapper.toUserContestRoleDto(contestDto, RoleEnum.ROLE_ADMIN))
                    .toList();
        }

        return userContestRoleRepository.findAllByUserEmail(userEmail).stream()
                .map(contestMapper::toUserContestRoleDto)
                .toList();
    }

    @Override
    public ContestDto updateContest(String userEmail, long id, ContestDto payload) {
        Contest contest = helpers.extractById(id, contestRepository);

        if (userHasNoWriteAccess(userEmail, id)) {
            throw new ForbiddenException("User has no write access.");
        }

        contest.setName(payload.getName());
        contest.setStartTime(payload.getStartTime());
        contest.setEndTime(payload.getEndTime());
        contest.setFreezeTime(payload.getFreezeTime());
        contest.setUnfreezeTime(payload.getUnfreezeTime());
        contest.setPublicScoreboard(payload.isPublicScoreboard());
        contestRepository.save(contest);

        return ContestDto.builder()
                .id(contest.getId())
                .name(contest.getName())
                .startTime(contest.getStartTime())
                .endTime(contest.getEndTime())
                .freezeTime(contest.getFreezeTime())
                .unfreezeTime(contest.getUnfreezeTime())
                .publicScoreboard(contest.isPublicScoreboard())
                .build();
    }

    @Override
    public List<UserContestRoleDto> getUserContestRoles(long id) {
        ContestDto contest = contestMapper.toContestDto(helpers.extractById(id, contestRepository));
        User admin = adminService.getAdmin();

        final Map<String, UserContestRole> userContestRoles = userContestRoleRepository.findAllByContestId(id)
                .stream()
                .collect(Collectors.toMap(userContestRole -> userContestRole.getUser().getEmail(), Function.identity()));

        return userRepository.findAll().stream()
                .map(user -> {
                    UserContestRoleDto userContestRoleDto = UserContestRoleDto.builder()
                            .contest(contest)
                            .user(userMapper.toDto(user))
                            .role(RoleEnum.ROLE_NOTHING)
                            .build();

                    if (userContestRoles.containsKey(user.getEmail())) {
                        userContestRoleDto.setRole(userContestRoles.get(user.getEmail()).getRole());
                    }

                    if (user.getEmail().equals(admin.getEmail())) {
                        userContestRoleDto.setRole(RoleEnum.ROLE_ADMIN);
                    }

                    return userContestRoleDto;
                })
                .toList();
    }

    @Override
    public UserContestRoleDto updateUserContestRole(String principalEmail, long contestId, long userId, RoleEnum role) {
        if (role == RoleEnum.ROLE_ADMIN) {
            throw new BadRequestException("Cannot change role to admin.");
        }

        if (!adminService.isAdmin(principalEmail)) {
            UserContestRole principalContestRole = extractUserContestRole(contestId, principalEmail);

            if (!principalContestRole.getRole().equals(RoleEnum.ROLE_MODERATOR) && !principalContestRole.getRole().equals(RoleEnum.ROLE_ADMIN)) {
                throw new ForbiddenException("Cannot change user role.");
            }
        }

        Contest contest = helpers.extractById(contestId, contestRepository);

        User user = helpers.extractById(userId, userRepository);

        UserContestRole userContestRole = userContestRoleRepository
                .findByContestIdAndUserId(contestId, userId)
                .orElse(UserContestRole.builder()
                        .contest(contest)
                        .user(user)
                        .build());

        userContestRole.setRole(role);

        userContestRole = userContestRoleRepository.save(userContestRole);

        return contestMapper.toUserContestRoleDto(userContestRole);
    }

    @Override
    public List<TeamResponse> getRegisteredTeams(long id) {

        return helpers.extractById(id, contestRepository).getTeams().stream()
                .map(teamMapper::toTeamDto)
                .toList();
    }

    private UserContestRole extractUserContestRole(long contestId, String userEmail) {
        return userContestRoleRepository.findByContestIdAndUserEmail(contestId, userEmail)
                .orElseThrow(() -> new NotFoundException("Incorrect contest id or user email"));
    }

    public boolean userHasNoWriteAccess(String userEmail, long contestId) {
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
