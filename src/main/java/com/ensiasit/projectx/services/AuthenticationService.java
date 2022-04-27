package com.ensiasit.projectx.services;

import com.ensiasit.projectx.dto.LoginRequestDto;
import com.ensiasit.projectx.dto.LoginResponseDto;

public interface AuthenticationService {
    LoginResponseDto authenticate(LoginRequestDto loginRequest);
}
