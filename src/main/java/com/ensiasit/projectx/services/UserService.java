package com.ensiasit.projectx.services;

import com.ensiasit.projectx.dto.UserDto;

import java.util.List;

public interface UserService {
    UserDto updateByEmail(String userEmail, UserDto userDto);

    UserDto addOne(String userEmail, UserDto payload);

    UserDto deleteOne(String userEmail, long id);

    UserDto updateOne(String userEmail, long id, UserDto payload);

    List<UserDto> getAll();

    UserDto findByEmail(String userEmail);

    UserDto getOneById(String userEmail, long id);
}
