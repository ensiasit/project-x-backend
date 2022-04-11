package com.ensiasit.projectx.services;

import com.ensiasit.projectx.dto.request.RegisterRequest;
import com.ensiasit.projectx.models.User;

public interface AuthService {

    public User registerUser(RegisterRequest registerRequest);
}
