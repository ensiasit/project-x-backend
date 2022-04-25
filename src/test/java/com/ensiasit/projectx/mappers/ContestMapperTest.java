package com.ensiasit.projectx.mappers;

import com.ensiasit.projectx.dto.ContestDto;
import com.ensiasit.projectx.dto.UserContestRoleDto;
import com.ensiasit.projectx.models.Contest;
import com.ensiasit.projectx.models.User;
import com.ensiasit.projectx.models.UserContestRole;
import com.ensiasit.projectx.utils.RoleEnum;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class ContestMapperTest {
    @InjectMocks
    private ContestMapper contestMapper;

    @Test
    void given_dto_should_return_contest_when_fromContestDto() {
        ContestDto dto = ContestDto.builder()
                .id(1L)
                .name("name")
                .startTime(LocalDateTime.now().plusDays(1))
                .endTime(LocalDateTime.now().plusDays(2))
                .freezeTime(LocalDateTime.now().plusDays(3))
                .unfreezeTime(LocalDateTime.now().plusDays(4))
                .publicScoreboard(false)
                .build();

        Contest contest = contestMapper.fromContestDto(dto);

        assertThat(contest.getId()).isEqualTo(dto.getId());
        assertThat(contest.getName()).isEqualTo(dto.getName());
        assertThat(contest.getStartTime()).isEqualTo(dto.getStartTime());
        assertThat(contest.getEndTime()).isEqualTo(dto.getEndTime());
        assertThat(contest.getFreezeTime()).isEqualTo(dto.getFreezeTime());
        assertThat(contest.getUnfreezeTime()).isEqualTo(dto.getUnfreezeTime());
        assertThat(contest.isPublicScoreboard()).isEqualTo(dto.isPublicScoreboard());
    }

    @Test
    void given_contest_should_return_dto_when_toContestDto() {
        Contest contest = Contest.builder()
                .id(1L)
                .name("name")
                .startTime(LocalDateTime.now().plusDays(1))
                .endTime(LocalDateTime.now().plusDays(2))
                .freezeTime(LocalDateTime.now().plusDays(3))
                .unfreezeTime(LocalDateTime.now().plusDays(4))
                .publicScoreboard(false)
                .build();

        ContestDto dto = contestMapper.toContestDto(contest);

        assertThat(dto.getId()).isEqualTo(contest.getId());
        assertThat(dto.getName()).isEqualTo(contest.getName());
        assertThat(dto.getStartTime()).isEqualTo(contest.getStartTime());
        assertThat(dto.getEndTime()).isEqualTo(contest.getEndTime());
        assertThat(dto.getFreezeTime()).isEqualTo(contest.getFreezeTime());
        assertThat(dto.getUnfreezeTime()).isEqualTo(contest.getUnfreezeTime());
        assertThat(dto.isPublicScoreboard()).isEqualTo(contest.isPublicScoreboard());
    }

    @Test
    void given_user_contest_role_return_dto_when_toUserContestRoleDto() {
        Contest contest = Contest.builder()
                .id(1L)
                .name("name")
                .startTime(LocalDateTime.now().plusDays(1))
                .endTime(LocalDateTime.now().plusDays(2))
                .freezeTime(LocalDateTime.now().plusDays(3))
                .unfreezeTime(LocalDateTime.now().plusDays(4))
                .publicScoreboard(false)
                .build();
        User user = mock(User.class);
        UserContestRole userContestRole = UserContestRole.builder()
                .role(RoleEnum.ROLE_NOTHING)
                .contest(contest)
                .user(user)
                .build();

        UserContestRoleDto dto = contestMapper.toUserContestRoleDto(userContestRole);

        assertThat(dto.getRole()).isEqualTo(userContestRole.getRole());
        assertThat(dto.getContest().getId()).isEqualTo(contest.getId());
        assertThat(dto.getContest().getName()).isEqualTo(contest.getName());
        assertThat(dto.getContest().getStartTime()).isEqualTo(contest.getStartTime());
        assertThat(dto.getContest().getEndTime()).isEqualTo(contest.getEndTime());
        assertThat(dto.getContest().getFreezeTime()).isEqualTo(contest.getFreezeTime());
        assertThat(dto.getContest().getUnfreezeTime()).isEqualTo(contest.getUnfreezeTime());
        assertThat(dto.getContest().isPublicScoreboard()).isEqualTo(contest.isPublicScoreboard());
        assertThat(dto.getUser()).isNull();
    }

    @Test
    void given_contest_dto_and_role_return_dto_when_toUserContestRoleDto() {
        ContestDto contestDto = mock(ContestDto.class);
        RoleEnum role = RoleEnum.ROLE_ADMIN;

        UserContestRoleDto dto = contestMapper.toUserContestRoleDto(contestDto, role);

        assertThat(dto.getRole()).isEqualTo(role);
        assertThat(dto.getContest()).isEqualTo(contestDto);
        assertThat(dto.getUser()).isNull();
    }
}