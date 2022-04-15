package com.ensiasit.projectx.services;

import com.ensiasit.projectx.dto.ContestDto;
import com.ensiasit.projectx.models.Contest;
import com.ensiasit.projectx.repositories.ContestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ContestServiceImpl implements ContestService {
    private final ContestRepository contestRepository;

    @Override
    public List<Contest> getAll() {
        return contestRepository.findAll();
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
                .name(contest.getName())
                .startTime(contest.getStartTime())
                .endTime(contest.getEndTime())
                .freezeTime(contest.getFreezeTime())
                .unfreezeTime(contest.getUnfreezeTime())
                .publicScoreboard(contest.isPublicScoreboard())
                .build();
    }

    @Override
    public Optional<ContestDto> getContest(Long id) {
        Optional<Contest> contestOptional = contestRepository.findById(id);

        if (contestOptional.isEmpty()) {
            return Optional.empty();
        }

        Contest contest = contestOptional.get();

        return Optional.of(ContestDto.builder()
                .name(contest.getName())
                .startTime(contest.getStartTime())
                .endTime(contest.getEndTime())
                .freezeTime(contest.getFreezeTime())
                .unfreezeTime(contest.getUnfreezeTime())
                .publicScoreboard(contest.isPublicScoreboard())
                .build());
    }

    @Override
    public void deleteContest(Long id) {
        contestRepository.deleteById(id);
    }
}
