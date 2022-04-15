package com.ensiasit.projectx.dto;

import com.ensiasit.projectx.utils.RoleEnum;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
public class RegisterRequest {
    @NotBlank
    private String username;

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String password;

    @NotBlank
    private long contestId;

    @NotBlank
    private RoleEnum role;
}
