package com.ensiasit.projectx.services;

import com.ensiasit.projectx.dto.UserDto;

import java.util.List;

public interface UserService {
    List<UserDto> getAll();

    UserDto getOne(String email);

    UserDto getOne(long id);

    UserDto addOne(UserDto payload);

    UserDto deleteOne(long id);

    UserDto updateOne(long id, UserDto payload);

    UserDto updateOne(String email, UserDto userDto);
}
