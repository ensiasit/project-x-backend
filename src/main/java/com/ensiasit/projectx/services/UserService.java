package com.ensiasit.projectx.services;

import com.ensiasit.projectx.dto.UserDto;

public interface UserService {
    UserDto updateByEmail(String userEmail, UserDto userDto);
}
