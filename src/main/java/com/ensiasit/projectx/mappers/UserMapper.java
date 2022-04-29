package com.ensiasit.projectx.mappers;

import com.ensiasit.projectx.dto.UserDto;
import com.ensiasit.projectx.models.User;
import com.ensiasit.projectx.services.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import static com.ensiasit.projectx.utils.Constants.HIDDEN_PASSWORD;

@Component
@RequiredArgsConstructor
public class UserMapper {
    private final PasswordEncoder encoder;
    private final AdminService adminService;

    public User fromDto(UserDto userDto) {
        return User.builder()
                .email(userDto.getEmail())
                .username(userDto.getUsername())
                .password(encoder.encode(userDto.getPassword()))
                .build();
    }

    public UserDto toDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .username(user.getUsername())
                .password(HIDDEN_PASSWORD)
                .admin(adminService.isAdmin(user.getEmail()))
                .build();
    }
}
