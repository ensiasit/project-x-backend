package com.ensiasit.projectx.services;

import com.ensiasit.projectx.annotations.SecureAdmin;
import com.ensiasit.projectx.dto.UserDto;
import com.ensiasit.projectx.exceptions.NotFoundException;
import com.ensiasit.projectx.mappers.UserMapper;
import com.ensiasit.projectx.models.User;
import com.ensiasit.projectx.repositories.UserContestRoleRepository;
import com.ensiasit.projectx.repositories.UserRepository;
import com.ensiasit.projectx.utils.Helpers;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserContestRoleRepository userContestRoleRepository;
    private final PasswordEncoder encoder;
    private final UserMapper userMapper;
    private final Helpers helpers;

    @Override
    public UserDto updateByEmail(String userEmail, UserDto userDto) {
        User user = extractByEmail(userEmail);

        user.setEmail(userDto.getEmail());
        user.setUsername(userDto.getUsername());
        user.setPassword(encoder.encode(userDto.getPassword()));

        user = userRepository.save(user);

        return userMapper.toDto(user);
    }

    @SecureAdmin
    @Override
    public UserDto addOne(String userEmail, UserDto payload) {
        User user = userRepository.save(userMapper.fromDto(payload));

        return userMapper.toDto(user);
    }

    @SecureAdmin
    @Override
    @Transactional
    public UserDto deleteOne(String userEmail, long id) {
        User user = helpers.extractById(id, userRepository);

        userContestRoleRepository.deleteAllByUserId(id);
        userRepository.delete(user);

        return userMapper.toDto(user);
    }

    @SecureAdmin
    @Override
    public UserDto updateOne(String userEmail, long id, UserDto payload) {
        User user = helpers.extractById(id, userRepository);

        user.setUsername(payload.getUsername());
        user.setEmail(payload.getEmail());
        user.setPassword(encoder.encode(payload.getPassword()));

        user = userRepository.save(user);

        return userMapper.toDto(user);
    }

    @Override
    public List<UserDto> getAll() {
        return userRepository.findAll().stream()
                .map(userMapper::toDto)
                .toList();
    }

    @Override
    public UserDto findByEmail(String userEmail) {
        return userMapper.toDto(extractByEmail(userEmail));
    }

    @SecureAdmin
    @Override
    public UserDto getOneById(String userEmail, long id) {
        return userMapper.toDto(helpers.extractById(id, userRepository));
    }

    private User extractByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Incorrect user email"));
    }
}
