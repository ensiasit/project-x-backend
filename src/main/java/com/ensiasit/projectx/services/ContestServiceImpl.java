package com.ensiasit.projectx.services;

import com.ensiasit.projectx.dto.ContestDto;
import com.ensiasit.projectx.dto.UserContestRoleDto;
import com.ensiasit.projectx.exceptions.NotFoundException;
import com.ensiasit.projectx.mappers.ContestMapper;
import com.ensiasit.projectx.models.Contest;
import com.ensiasit.projectx.repositories.ContestRepository;
import com.ensiasit.projectx.repositories.UserContestRoleRepository;
import com.ensiasit.projectx.utils.RoleEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
    public ContestDto createContest(ContestDto contestDto) {
        Contest contest = contestRepository.save(contestMapper.fromContestDto(contestDto));

        return contestMapper.toContestDto(contest);
    }

    @Override
    public ContestDto getContest(long id) {
        Contest contest = extractContest(id);

        return contestMapper.toContestDto(contest);
    }

    @Override
    public ContestDto deleteContest(long id) {
        Contest contest = extractContest(id);
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
    public ContestDto updateContest(long id, ContestDto payload) {
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

    private Contest extractContest(long id) {
        Optional<Contest> contestOptional = contestRepository.findById(id);

        if (contestOptional.isEmpty()) {
            throw new NotFoundException("Incorrect contest id");
        }

        return contestOptional.get();
    }
}
