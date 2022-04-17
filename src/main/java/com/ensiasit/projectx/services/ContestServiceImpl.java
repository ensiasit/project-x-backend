package com.ensiasit.projectx.services;

import com.ensiasit.projectx.dto.ContestDto;
import com.ensiasit.projectx.dto.UserContestRoleDto;
import com.ensiasit.projectx.exceptions.NotFoundException;
import com.ensiasit.projectx.models.Contest;
import com.ensiasit.projectx.repositories.ContestRepository;
import com.ensiasit.projectx.repositories.UserContestRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ContestServiceImpl implements ContestService {
    private final ContestRepository contestRepository;
    private final UserContestRoleRepository userContestRoleRepository;

    @Override
    public List<ContestDto> getAll() {
        return contestRepository.findAll().stream()
                .map(contest -> ContestDto.builder()
                        .id(contest.getId())
                        .name(contest.getName())
                        .startTime(contest.getStartTime())
                        .endTime(contest.getEndTime())
                        .freezeTime(contest.getFreezeTime())
                        .unfreezeTime(contest.getUnfreezeTime())
                        .publicScoreboard(contest.isPublicScoreboard())
                        .build())
                .toList();
    }

    @Override
    public ContestDto createContest(ContestDto contestDto) {
        Contest contest = contestRepository.save(Contest.builder()
                .name(contestDto.getName())
                .startTime(contestDto.getStartTime())
                .endTime(contestDto.getEndTime())
                .freezeTime(contestDto.getFreezeTime())
                .unfreezeTime(contestDto.getUnfreezeTime())
                .publicScoreboard(contestDto.isPublicScoreboard())
                .build());

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
    public ContestDto getContest(long id) {
        Optional<Contest> contestOptional = contestRepository.findById(id);

        if (contestOptional.isEmpty()) {
            throw new NotFoundException("Incorrect contest id");
        }

        Contest contest = contestOptional.get();

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
    public ContestDto deleteContest(long id) {
        Contest contest = contestRepository.deleteContestById(id);

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
    public List<UserContestRoleDto> getAllUserContests(String userEmail) {
        return userContestRoleRepository.findAllByUserEmail(userEmail).stream()
                .map(userContestRole -> UserContestRoleDto.builder()
                        .contest(userContestRole.getContest())
                        .role(userContestRole.getRole())
                        .build())
                .toList();
    }
}
