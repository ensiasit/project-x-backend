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
        Optional<Contest> contestOptional = contestRepository.findById(id);

        if (contestOptional.isEmpty()) {
            throw new NotFoundException("Incorrect contest id");
        }

        Contest contest = contestOptional.get();

        return contestMapper.toContestDto(contest);
    }

    @Override
    public ContestDto deleteContest(long id) {
        Contest contest = contestRepository.deleteContestById(id);

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
}
