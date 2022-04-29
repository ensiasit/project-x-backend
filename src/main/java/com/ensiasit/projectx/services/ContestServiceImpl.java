package com.ensiasit.projectx.services;

import com.ensiasit.projectx.annotations.SecureAdmin;
import com.ensiasit.projectx.dto.ContestDto;
import com.ensiasit.projectx.dto.UserContestRoleDto;
import com.ensiasit.projectx.exceptions.BadRequestException;
import com.ensiasit.projectx.exceptions.ForbiddenException;
import com.ensiasit.projectx.mappers.ContestMapper;
import com.ensiasit.projectx.models.Contest;
import com.ensiasit.projectx.models.User;
import com.ensiasit.projectx.models.UserContestRole;
import com.ensiasit.projectx.repositories.ContestRepository;
import com.ensiasit.projectx.repositories.UserContestRoleRepository;
import com.ensiasit.projectx.repositories.UserRepository;
import com.ensiasit.projectx.utils.Helpers;
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
    private final Helpers helpers;

    @Override
    public List<ContestDto> getAll() {
        return contestRepository.findAll().stream()
                .map(contestMapper::toContestDto)
                .toList();
    }

    @Override
    public ContestDto getOne(long id) {
        Contest contest = helpers.extractById(id, contestRepository);

        return contestMapper.toContestDto(contest);
    }

    @SecureAdmin
    @Transactional
    @Override
    public ContestDto addOne(ContestDto contestDto) {
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

    @SecureAdmin
    @Transactional
    @Override
    public ContestDto deleteOne(long id) {
        Contest contest = helpers.extractById(id, contestRepository);

        userContestRoleRepository.deleteAllByContestId(id);

        contestRepository.deleteById(id);

        return contestMapper.toContestDto(contest);
    }

    @SecureAdmin
    @Override
    public ContestDto updateOne(long id, ContestDto payload) {
        Contest contest = helpers.extractById(id, contestRepository);

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
    public List<UserContestRoleDto> getAllRolesByUserEmail(String email) {
        return userContestRoleRepository.findAllByUserEmail(email).stream()
                .map(contestMapper::toUserContestRoleDto)
                .toList();
    }

    @Override
    public List<UserContestRoleDto> getAllRoles(long id) {
        return userContestRoleRepository.findAllByContestId(id).stream()
                .map(contestMapper::toUserContestRoleDto)
                .toList();
    }

    @Override
    public UserContestRoleDto updateRole(String principalEmail, long contestId, long userId, RoleEnum role) {
        if (role == RoleEnum.ROLE_ADMIN) {
            throw new BadRequestException("Cannot change role to admin.");
        }

        checkWriteAccess(principalEmail, contestId);

        UserContestRole userContestRole = helpers.extract(() -> userContestRoleRepository.findByContestIdAndUserId(contestId, userId));

        userContestRole.setRole(role);

        return contestMapper.toUserContestRoleDto(userContestRoleRepository.save(userContestRole));
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
