package com.ensiasit.projectx.mappers;

import com.ensiasit.projectx.dto.UserDto;
import com.ensiasit.projectx.models.User;
import com.ensiasit.projectx.services.AdminService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static com.ensiasit.projectx.utils.Constants.HIDDEN_PASSWORD;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserMapperTest {
    @Mock
    private PasswordEncoder encoder;

    @Mock
    private AdminService adminService;

    @InjectMocks
    private UserMapper userMapper;

    @Test
    void given_dto_should_return_user_with_encoded_password_and_empty_id_and_when_fromDto() {
        String encodedPassword = "encodedPassword";
        UserDto dto = UserDto.builder()
                .id(1L)
                .email("email")
                .username("username")
                .password("password")
                .admin(true)
                .build();

        when(encoder.encode(dto.getPassword())).thenReturn(encodedPassword);

        User user = userMapper.fromDto(dto);

        assertThat(user.getId()).isNull();
        assertThat(user.getEmail()).isEqualTo(dto.getEmail());
        assertThat(user.getUsername()).isEqualTo(dto.getUsername());
        assertThat(user.getPassword()).isEqualTo(encodedPassword);
    }

    @Test
    void given_user_should_return_dto_with_correct_admin_and_hidden_password_when_toDto() {
        boolean admin = true;
        User user = User.builder()
                .id(1L)
                .email("email")
                .username("username")
                .password("password")
                .build();

        when(adminService.isAdmin(user.getEmail())).thenReturn(admin);

        UserDto dto = userMapper.toDto(user);

        assertThat(dto.getId()).isEqualTo(user.getId());
        assertThat(dto.getEmail()).isEqualTo(user.getEmail());
        assertThat(dto.getUsername()).isEqualTo(user.getUsername());
        assertThat(dto.getPassword()).isEqualTo(HIDDEN_PASSWORD);
        assertThat(dto.isAdmin()).isEqualTo(admin);
    }
}