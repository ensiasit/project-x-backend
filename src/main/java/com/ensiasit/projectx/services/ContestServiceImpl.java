package com.ensiasit.projectx.services;

import com.ensiasit.projectx.dto.ContestDto;
import com.ensiasit.projectx.dto.UserContestRoleDto;
import com.ensiasit.projectx.exceptions.BadRequestException;
import com.ensiasit.projectx.exceptions.ForbiddenException;
import com.ensiasit.projectx.exceptions.NotFoundException;
import com.ensiasit.projectx.mappers.ContestMapper;
import com.ensiasit.projectx.mappers.UserMapper;
import com.ensiasit.projectx.models.Contest;
import com.ensiasit.projectx.models.User;
import com.ensiasit.projectx.models.UserContestRole;
import com.ensiasit.projectx.repositories.ContestRepository;
import com.ensiasit.projectx.repositories.UserContestRoleRepository;
import com.ensiasit.projectx.repositories.UserRepository;
import com.ensiasit.projectx.utils.RoleEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ContestServiceImpl implements ContestService {
    private final ContestRepository contestRepository;
    private final UserContestRoleRepository userContestRoleRepository;
    private final AdminService adminService;
    private final ContestMapper contestMapper;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public List<ContestDto> getAll() {
        return contestRepository.findAll().stream()
                .map(contestMapper::toContestDto)
                .toList();
    }

    @Transactional
    @Override
    public ContestDto createContest(String userEmail, ContestDto contestDto) {
        checkAdminAccess(userEmail);
        User adminUser = adminService.getAdmin();

        Contest contest = contestRepository.save(contestMapper.fromContestDto(contestDto));

        List<UserContestRole> userContestRoles = userRepository.findAll().stream()
                .map(user -> UserContestRole.builder()
                        .user(user)
                        .contest(contest)
                        .role(user.getEmail().equals(adminUser.getEmail()) ? RoleEnum.ROLE_ADMIN : RoleEnum.ROLE_USER)
                        .build())
                .toList();

        userContestRoleRepository.saveAll(userContestRoles);

        return contestMapper.toContestDto(contest);
    }

    @Override
    public ContestDto getContest(long id) {
        Contest contest = extractContest(id);

        return contestMapper.toContestDto(contest);
    }

    @Transactional
    @Override
    public ContestDto deleteContest(String userEmail, long id) {
        checkAdminAccess(userEmail);

        Contest contest = extractContest(id);

        userContestRoleRepository.deleteAllByContestId(id);

        contestRepository.deleteById(id);

        return contestMapper.toContestDto(contest);
    }

    @Override
    public List<UserContestRoleDto> getAllUserContests(String userEmail) {
        return userContestRoleRepository.findAllByUserEmail(userEmail).stream()
                .map(contestMapper::toUserContestRoleDto)
                .toList();
    }

    @Override
    public ContestDto updateContest(String userEmail, long id, ContestDto payload) {
        checkWriteAccess(userEmail, id);

        Contest contest = extractContest(id);

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
        return userContestRoleRepository.findAllByContestId(id).stream()
                .map(contestMapper::toUserContestRoleDto)
                .toList();
    }

    @Override
    public UserContestRoleDto updateUserContestRole(String principalEmail, long contestId, long userId, RoleEnum role) {
        if (role == RoleEnum.ROLE_ADMIN) {
            throw new BadRequestException("Cannot change role to admin.");
        }

        checkWriteAccess(principalEmail, contestId);

        UserContestRole userContestRole = extractUserContestRole(contestId, userId);

        userContestRole.setRole(role);

        return contestMapper.toUserContestRoleDto(userContestRoleRepository.save(userContestRole));
    }

    private Contest extractContest(long id) {
        Optional<Contest> contestOptional = contestRepository.findById(id);

        if (contestOptional.isEmpty()) {
            throw new NotFoundException("Incorrect contest id");
        }

        return contestOptional.get();
    }

    private UserContestRole extractUserContestRole(long contestId, long userId) {
        return userContestRoleRepository.findByContestIdAndUserId(contestId, userId)
                .orElseThrow(() -> new NotFoundException("Incorrect contest id or user id"));
    }

    private void checkAdminAccess(String userEmail) {
        if (!adminService.isAdmin(userEmail)) {
            throw new ForbiddenException("User is not admin");
        }
    }

    private void checkWriteAccess(String userEmail, long contestId) {
        if (adminService.isAdmin(userEmail)) {
            return;
        }

        Optional<UserContestRole> userContestRole = userContestRoleRepository.findByContestIdAndUserEmail(contestId, userEmail);

        if (userContestRole.isEmpty() || userContestRole.get().getRole() == RoleEnum.ROLE_USER) {
            throw new ForbiddenException("User has no write access");
        }
    }
}
