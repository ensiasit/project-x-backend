package com.ensiasit.projectx.services;

import com.ensiasit.projectx.annotations.SecureAdmin;
import com.ensiasit.projectx.dto.UserDto;
import com.ensiasit.projectx.mappers.UserMapper;
import com.ensiasit.projectx.models.User;
import com.ensiasit.projectx.models.UserContestRole;
import com.ensiasit.projectx.repositories.ContestRepository;
import com.ensiasit.projectx.repositories.UserContestRoleRepository;
import com.ensiasit.projectx.repositories.UserRepository;
import com.ensiasit.projectx.utils.Helpers;
import com.ensiasit.projectx.utils.RoleEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private final UserMapper userMapper;
    private final ContestRepository contestRepository;
    private final UserContestRoleRepository userContestRoleRepository;
    private final Helpers helpers;

    @Override
    public List<UserDto> getAll() {
        return userRepository.findAll().stream()
                .map(userMapper::toDto)
                .toList();
    }

    @Override
    public UserDto getOne(String email) {
        User user = helpers.extract(() -> userRepository.findByEmail(email));

        return userMapper.toDto(user);
    }

    @Override
    public UserDto getOne(long id) {
        User user = helpers.extractById(id, userRepository);

        return userMapper.toDto(user);
    }

    @SecureAdmin
    @Transactional
    @Override
    public UserDto addOne(UserDto payload) {
        User user = userRepository.save(userMapper.fromDto(payload));

        List<UserContestRole> userContestRoles = contestRepository.findAll().stream()
                .map(contest -> UserContestRole.builder()
                        .user(user)
                        .contest(contest)
                        .role(RoleEnum.ROLE_USER)
                        .build())
                .toList();

        userContestRoleRepository.saveAll(userContestRoles);

        return userMapper.toDto(user);
    }

    @SecureAdmin
    @Transactional
    @Override
    public UserDto deleteOne(long id) {
        User user = helpers.extractById(id, userRepository);

        userContestRoleRepository.deleteAllByUserId(id);

        userRepository.delete(user);

        return userMapper.toDto(user);
    }

    @SecureAdmin
    @Override
    public UserDto updateOne(long id, UserDto payload) {
        User user = helpers.extractById(id, userRepository);

        user.setUsername(payload.getUsername());
        user.setEmail(payload.getEmail());
        user.setPassword(encoder.encode(payload.getPassword()));

        user = userRepository.save(user);

        return userMapper.toDto(user);
    }

    @Override
    public UserDto updateOne(String email, UserDto userDto) {
        User user = helpers.extract(() -> userRepository.findByEmail(email));

        user.setEmail(userDto.getEmail());
        user.setUsername(userDto.getUsername());
        user.setPassword(encoder.encode(userDto.getPassword()));

        user = userRepository.save(user);

        return userMapper.toDto(user);
    }
}
