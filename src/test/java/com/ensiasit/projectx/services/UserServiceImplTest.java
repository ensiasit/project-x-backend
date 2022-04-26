package com.ensiasit.projectx.services;

import com.ensiasit.projectx.dto.UserDto;
import com.ensiasit.projectx.exceptions.ForbiddenException;
import com.ensiasit.projectx.exceptions.NotFoundException;
import com.ensiasit.projectx.mappers.UserMapper;
import com.ensiasit.projectx.models.Contest;
import com.ensiasit.projectx.models.User;
import com.ensiasit.projectx.models.UserContestRole;
import com.ensiasit.projectx.repositories.ContestRepository;
import com.ensiasit.projectx.repositories.UserContestRoleRepository;
import com.ensiasit.projectx.repositories.UserRepository;
import com.ensiasit.projectx.utils.RoleEnum;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
    private AdminService adminService;

    @Mock
    private UserContestRoleRepository userContestRoleRepository;

    @Mock
    private ContestRepository contestRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void given_not_existing_user_email_should_throw_exception_when_updateByEmail() {
        String email = "email";
        UserDto dto = mock(UserDto.class);

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        assertThrows(
                NotFoundException.class,
                () -> userService.updateByEmail(email, dto)
        );
    }

    @Test
    void given_existing_user_email_should_update_when_updateByEmail() {
        String email = "email";
        String encodedPassword = "encodedPassword";
        UserDto dto = UserDto.builder()
                .email("newEmail")
                .username("newUsername")
                .password("newPassword")
                .build();
        User user = User.builder().build();

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(encoder.encode(dto.getPassword())).thenReturn(encodedPassword);
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toDto(user)).thenReturn(dto);

        UserDto userDto = userService.updateByEmail(email, dto);

        verify(userRepository).findByEmail(email);
        verify(encoder).encode(dto.getPassword());
        verify(userRepository).save(user);
        verify(userMapper).toDto(user);

        assertThat(userDto).isEqualTo(dto);
    }

    @Test
    void given_not_admin_should_throw_exception_when_addOne() {
        String email = "email";
        UserDto dto = mock(UserDto.class);

        when(adminService.isAdmin(email)).thenReturn(false);

        assertThrows(
                ForbiddenException.class,
                () -> userService.addOne(email, dto)
        );
    }

    @Test
    void given_admin_should_add_user_when_addOne() {
        String email = "email";
        UserDto dto = mock(UserDto.class);
        User user = mock(User.class);
        Contest contest = mock(Contest.class);
        List<Contest> contests = List.of(contest);
        List<UserContestRole> userContestRoles = List.of(UserContestRole.builder()
                .user(user)
                .contest(contest)
                .role(RoleEnum.ROLE_USER)
                .build());

        when(adminService.isAdmin(email)).thenReturn(true);
        when(userMapper.fromDto(dto)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toDto(user)).thenReturn(dto);
        when(contestRepository.findAll()).thenReturn(contests);

        UserDto userDto = userService.addOne(email, dto);

        verify(userRepository).save(user);
        verify(userContestRoleRepository).saveAll(userContestRoles);

        assertThat(userDto).isEqualTo(dto);
    }

    @Test
    void given_not_admin_should_throw_exception_when_deleteOne() {
        String email = "email";

        when(adminService.isAdmin(email)).thenReturn(false);

        assertThrows(
                ForbiddenException.class,
                () -> userService.deleteOne(email, 1L)
        );
    }

    @Test
    void given_admin_and_not_correct_id_should_throw_exception_when_deleteOne() {
        String email = "email";
        long id = 1L;

        when(adminService.isAdmin(email)).thenReturn(true);
        when(userRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(
                NotFoundException.class,
                () -> userService.deleteOne(email, id)
        );
    }

    @Test
    void given_admin_should_delete_user_when_deleteOne() {
        String email = "email";
        User user = User.builder().id(1L).build();
        UserDto dto = mock(UserDto.class);

        when(adminService.isAdmin(email)).thenReturn(true);
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        doNothing().when(userContestRoleRepository).deleteAllByUserId(user.getId());
        doNothing().when(userRepository).delete(user);
        when(userMapper.toDto(user)).thenReturn(dto);

        UserDto userDto = userService.deleteOne(email, user.getId());

        verify(userContestRoleRepository).deleteAllByUserId(user.getId());
        verify(userRepository).delete(user);

        assertThat(userDto).isEqualTo(dto);
    }

    @Test
    void given_not_admin_should_throw_exception_when_updateOne() {
        String email = "email";
        UserDto dto = mock(UserDto.class);

        when(adminService.isAdmin(email)).thenReturn(false);

        assertThrows(
                ForbiddenException.class,
                () -> userService.updateOne(email, 1L, dto)
        );
    }

    @Test
    void given_admin_and_not_correct_id_should_throw_exception_when_updateOne() {
        String email = "email";
        long id = 1L;
        UserDto dto = mock(UserDto.class);

        when(adminService.isAdmin(email)).thenReturn(true);
        when(userRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(
                NotFoundException.class,
                () -> userService.updateOne(email, id, dto)
        );
    }

    @Test
    void given_admin_and_correct_id_should_update_when_updateOne() {
        String email = "email";
        User user = User.builder().id(1L).build();
        String encodedPassword = "encodedPassword";
        UserDto dto = UserDto.builder()
                .email("email")
                .username("username")
                .password("password")
                .build();

        when(adminService.isAdmin(email)).thenReturn(true);
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(encoder.encode(dto.getPassword())).thenReturn(encodedPassword);
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toDto(user)).thenReturn(dto);

        UserDto userDto = userService.updateOne(email, user.getId(), dto);

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);

        verify(userRepository).save(userCaptor.capture());

        assertThat(userCaptor.getValue().getUsername()).isEqualTo(dto.getUsername());
        assertThat(userCaptor.getValue().getEmail()).isEqualTo(dto.getEmail());
        assertThat(userCaptor.getValue().getPassword()).isEqualTo(encodedPassword);
        assertThat(userDto).isEqualTo(dto);
    }

    @Test
    void should_return_all_users_when_getAll() {
        User user = mock(User.class);
        UserDto dto = mock(UserDto.class);

        when(userRepository.findAll()).thenReturn(List.of(user));
        when(userMapper.toDto(user)).thenReturn(dto);

        List<UserDto> dtos = userService.getAll();

        verify(userRepository).findAll();
        verify(userMapper).toDto(user);

        assertThat(dtos).hasSize(1);
        assertThat(dtos.get(0)).isEqualTo(dto);
    }

    @Test
    void given_not_existing_user_email_should_throw_exception_when_findByEmail() {
        String email = "email";

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        assertThrows(
                NotFoundException.class,
                () -> userService.findByEmail(email)
        );
    }

    @Test
    void given_existing_user_email_should_return_dto_when_findByEmail() {
        String email = "email";
        User user = mock(User.class);
        UserDto dto = mock(UserDto.class);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(userMapper.toDto(user)).thenReturn(dto);

        UserDto userDto = userService.findByEmail(email);

        assertThat(userDto).isEqualTo(dto);
    }

    @Test
    void given_not_admin_should_throw_exception_when_getOneById() {
        String email = "email";

        when(adminService.isAdmin(email)).thenReturn(false);

        assertThrows(
                ForbiddenException.class,
                () -> userService.getOneById(email, 1L)
        );
    }

    @Test
    void given_not_existing_user_id_should_throw_exception_when_getOneById() {
        String email = "email";
        long id = 1L;

        when(adminService.isAdmin(email)).thenReturn(true);
        when(userRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(
                NotFoundException.class,
                () -> userService.getOneById(email, id)
        );
    }

    @Test
    void given_existing_user_id_should_return_dto_when_getOneById() {
        String email = "email";
        User user = User.builder().id(1L).build();
        UserDto dto = mock(UserDto.class);

        when(adminService.isAdmin(email)).thenReturn(true);
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(userMapper.toDto(user)).thenReturn(dto);

        UserDto userDto = userService.getOneById(email, user.getId());

        assertThat(userDto).isEqualTo(dto);
    }
}