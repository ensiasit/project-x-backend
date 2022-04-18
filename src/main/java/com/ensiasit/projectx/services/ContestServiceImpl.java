package com.ensiasit.projectx.services;

import com.ensiasit.projectx.dto.ContestDto;
import com.ensiasit.projectx.dto.UserContestRoleDto;
import com.ensiasit.projectx.exceptions.ForbiddenException;
import com.ensiasit.projectx.exceptions.NotFoundException;
import com.ensiasit.projectx.mappers.ContestMapper;
import com.ensiasit.projectx.models.Contest;
import com.ensiasit.projectx.models.UserContestRole;
import com.ensiasit.projectx.repositories.ContestRepository;
import com.ensiasit.projectx.repositories.UserContestRoleRepository;
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

    @Override
    public List<ContestDto> getAll() {
        return contestRepository.findAll().stream()
                .map(contestMapper::toContestDto)
                .toList();
    }

    @Override
    public ContestDto createContest(String userEmail, ContestDto contestDto) {
        if (!adminService.isAdmin(userEmail)) {
            throw new ForbiddenException("User has no write access.");
        }

        Contest contest = contestRepository.save(contestMapper.fromContestDto(contestDto));

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
        Contest contest = extractContest(id);

        if (userHasNoWriteAccess(userEmail, id)) {
            throw new ForbiddenException("User has no write access.");
        }

        userContestRoleRepository.deleteAllByContestId(id);
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
        Contest contest = extractContest(id);

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

    private Contest extractContest(long id) {
        Optional<Contest> contestOptional = contestRepository.findById(id);

        if (contestOptional.isEmpty()) {
            throw new NotFoundException("Incorrect contest id");
        }

        return contestOptional.get();
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
