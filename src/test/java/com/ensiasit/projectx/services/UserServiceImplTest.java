package com.ensiasit.projectx.services;

import com.ensiasit.projectx.dto.UserDto;
import com.ensiasit.projectx.mappers.UserMapper;
import com.ensiasit.projectx.models.Contest;
import com.ensiasit.projectx.models.User;
import com.ensiasit.projectx.models.UserContestRole;
import com.ensiasit.projectx.repositories.ContestRepository;
import com.ensiasit.projectx.repositories.UserContestRoleRepository;
import com.ensiasit.projectx.repositories.UserRepository;
import com.ensiasit.projectx.utils.Helpers;
import com.ensiasit.projectx.utils.RoleEnum;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder encoder;

    @Mock
    private UserMapper userMapper;

    @Mock
    private UserContestRoleRepository userContestRoleRepository;

    @Mock
    private ContestRepository contestRepository;

    @Mock
    private Helpers helpers;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void should_return_all_users_when_getAll() {
        User user = mock(User.class);
        UserDto dto = mock(UserDto.class);

        when(userRepository.findAll()).thenReturn(List.of(user));
        when(userMapper.toDto(user)).thenReturn(dto);

        List<UserDto> dtoList = userService.getAll();

        verify(userRepository).findAll();
        verify(userMapper).toDto(user);

        assertThat(dtoList).hasSize(1);
        assertThat(dtoList.get(0)).isEqualTo(dto);
    }

    @Test
    void given_existing_user_email_should_return_dto_when_getOne() {
        String email = "email";
        User user = mock(User.class);
        UserDto dto = mock(UserDto.class);

        when(helpers.extract(any())).thenReturn(user);
        when(userMapper.toDto(user)).thenReturn(dto);

        UserDto userDto = userService.getOne(email);

        assertThat(userDto).isEqualTo(dto);
    }

    @Test
    void given_existing_user_id_should_return_dto_when_getOne() {
        User user = User.builder().id(1L).build();
        UserDto dto = mock(UserDto.class);

        when(helpers.extractById(user.getId(), userRepository)).thenReturn(user);
        when(userMapper.toDto(user)).thenReturn(dto);

        UserDto userDto = userService.getOne(user.getId());

        assertThat(userDto).isEqualTo(dto);
    }

    @Test
    void given_admin_should_add_user_when_addOne() {
        UserDto dto = mock(UserDto.class);
        User user = mock(User.class);
        RoleEnum role = RoleEnum.ROLE_USER;
        Contest contest = mock(Contest.class);
        List<Contest> contests = List.of(contest);

        when(userMapper.fromDto(dto)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);
        when(contestRepository.findAll()).thenReturn(contests);
        when(userMapper.toDto(user)).thenReturn(dto);

        UserDto userDto = userService.addOne(dto);

        verify(userRepository).save(user);

        ArgumentCaptor<List<UserContestRole>> listCaptor = ArgumentCaptor.forClass(List.class);

        verify(userContestRoleRepository).saveAll(listCaptor.capture());

        assertThat(listCaptor.getValue()).hasSize(1);
        assertThat(listCaptor.getValue().get(0).getUser()).isEqualTo(user);
        assertThat(listCaptor.getValue().get(0).getContest()).isEqualTo(contest);
        assertThat(listCaptor.getValue().get(0).getRole()).isEqualTo(role);

        assertThat(userDto).isEqualTo(dto);
    }

    @Test
    void given_admin_should_delete_user_when_deleteOne() {
        User user = User.builder().id(1L).build();
        UserDto dto = mock(UserDto.class);

        when(helpers.extractById(user.getId(), userRepository)).thenReturn(user);
        doNothing().when(userContestRoleRepository).deleteAllByUserId(user.getId());
        doNothing().when(userRepository).delete(user);
        when(userMapper.toDto(user)).thenReturn(dto);

        UserDto userDto = userService.deleteOne(user.getId());

        verify(userContestRoleRepository).deleteAllByUserId(user.getId());
        verify(userRepository).delete(user);

        assertThat(userDto).isEqualTo(dto);
    }


    @Test
    void given_existing_user_email_should_update_when_updateOne() {
        String email = "email";
        String encodedPassword = "encodedPassword";
        UserDto dto = UserDto.builder()
                .email("newEmail")
                .username("newUsername")
                .password("newPassword")
                .build();
        User user = User.builder().build();

        when(helpers.extract(any())).thenReturn(user);
        when(encoder.encode(dto.getPassword())).thenReturn(encodedPassword);
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toDto(user)).thenReturn(dto);

        UserDto userDto = userService.updateOne(email, dto);

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);

        verify(userRepository).save(userCaptor.capture());

        assertThat(userCaptor.getValue().getUsername()).isEqualTo(dto.getUsername());
        assertThat(userCaptor.getValue().getEmail()).isEqualTo(dto.getEmail());
        assertThat(userCaptor.getValue().getPassword()).isEqualTo(encodedPassword);

        assertThat(userDto).isEqualTo(dto);
    }

    @Test
    void given_admin_and_correct_id_should_update_when_updateOne() {
        User user = User.builder().id(1L).build();
        String encodedPassword = "encodedPassword";
        UserDto dto = UserDto.builder()
                .email("email")
                .username("username")
                .password("password")
                .build();

        when(helpers.extractById(user.getId(), userRepository)).thenReturn(user);
        when(encoder.encode(dto.getPassword())).thenReturn(encodedPassword);
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toDto(user)).thenReturn(dto);

        UserDto userDto = userService.updateOne(user.getId(), dto);

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);

        verify(userRepository).save(userCaptor.capture());

        assertThat(userCaptor.getValue().getUsername()).isEqualTo(dto.getUsername());
        assertThat(userCaptor.getValue().getEmail()).isEqualTo(dto.getEmail());
        assertThat(userCaptor.getValue().getPassword()).isEqualTo(encodedPassword);
        assertThat(userDto).isEqualTo(dto);
    }
}