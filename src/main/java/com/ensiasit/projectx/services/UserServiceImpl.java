package com.ensiasit.projectx.services;

import com.ensiasit.projectx.dto.UserDto;
import com.ensiasit.projectx.exceptions.ForbiddenException;
import com.ensiasit.projectx.exceptions.NotFoundException;
import com.ensiasit.projectx.mappers.UserMapper;
import com.ensiasit.projectx.models.User;
import com.ensiasit.projectx.models.UserContestRole;
import com.ensiasit.projectx.repositories.ContestRepository;
import com.ensiasit.projectx.repositories.UserContestRoleRepository;
import com.ensiasit.projectx.repositories.UserRepository;
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
    private final AdminService adminService;
    private final ContestRepository contestRepository;
    private final UserContestRoleRepository userContestRoleRepository;

    @Override
    public UserDto updateByEmail(String userEmail, UserDto userDto) {
        User user = extract(userEmail);

        user.setEmail(userDto.getEmail());
        user.setUsername(userDto.getUsername());
        user.setPassword(encoder.encode(userDto.getPassword()));

        user = userRepository.save(user);

        return userMapper.toDto(user);
    }

    @Transactional
    @Override
    public UserDto addOne(String userEmail, UserDto payload) {
        checkAdminAccess(userEmail);

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

    @Transactional
    @Override
    public UserDto deleteOne(String userEmail, long id) {
        checkAdminAccess(userEmail);

        User user = extract(id);

        userContestRoleRepository.deleteAllByUserId(id);

        userRepository.delete(user);

        return userMapper.toDto(user);
    }

    @Override
    public UserDto updateOne(String userEmail, long id, UserDto payload) {
        checkAdminAccess(userEmail);

        User user = extract(id);

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
        return userMapper.toDto(extract(userEmail));
    }

    @Override
    public UserDto getOneById(String userEmail, long id) {
        checkAdminAccess(userEmail);

        return userMapper.toDto(extract(id));
    }

    private User extract(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Incorrect user email"));
    }

    private User extract(long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Incorrect user id"));
    }

    private void checkAdminAccess(String userEmail) {
        if (!adminService.isAdmin(userEmail)) {
            throw new ForbiddenException("User is not admin");
        }
    }
}
