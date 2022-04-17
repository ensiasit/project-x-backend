package com.ensiasit.projectx.services;

import com.ensiasit.projectx.dto.UserDto;
import com.ensiasit.projectx.exceptions.BadRequestException;
import com.ensiasit.projectx.models.User;
import com.ensiasit.projectx.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;

    @Override
    public UserDto updateByEmail(String userEmail, UserDto userDto) {
        Optional<User> user = userRepository.findByEmail(userEmail);

        if (user.isEmpty()) {
            throw new BadRequestException("Incorrect user email");
        }

        if (!user.get().getEmail().equals(userEmail)) {
            throw new BadRequestException("Cannot update other user information");
        }

        User updatedUser = user.get();

        updatedUser.setEmail(userDto.getEmail());
        updatedUser.setUsername(userDto.getUsername());
        updatedUser.setPassword(encoder.encode(userDto.getPassword()));

        updatedUser = userRepository.save(updatedUser);

        return UserDto.builder()
                .username(updatedUser.getUsername())
                .email(updatedUser.getEmail())
                .password("HIDDEN")
                .build();
    }
}
