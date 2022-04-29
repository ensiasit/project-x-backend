package com.ensiasit.projectx.mappers;

import com.ensiasit.projectx.dto.ContestDto;
import com.ensiasit.projectx.dto.UserContestRoleDto;
import com.ensiasit.projectx.models.Contest;
import com.ensiasit.projectx.models.UserContestRole;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ContestMapper {
    private final UserMapper userMapper;

    public Contest fromContestDto(ContestDto contestDto) {
        return Contest.builder()
                .id(contestDto.getId())
                .name(contestDto.getName())
                .startTime(contestDto.getStartTime())
                .endTime(contestDto.getEndTime())
                .freezeTime(contestDto.getFreezeTime())
                .unfreezeTime(contestDto.getUnfreezeTime())
                .publicScoreboard(contestDto.isPublicScoreboard())
                .build();
    }

    public ContestDto toContestDto(Contest contest) {
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

    public UserContestRoleDto toUserContestRoleDto(UserContestRole userContestRole) {
        return UserContestRoleDto.builder()
                .user(userMapper.toDto(userContestRole.getUser()))
                .role(userContestRole.getRole())
                .contest(toContestDto(userContestRole.getContest()))
                .build();
    }
}
