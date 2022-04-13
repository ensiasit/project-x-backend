package com.ensiasit.projectx.dto.request;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.Set;

@Data
public class RegisterRequest {

    @NotBlank
    private String username;

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String password;

    private Set<String> roles;
}
