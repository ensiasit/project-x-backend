package com.ensiasit.projectx.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class UserDto {
    @NotBlank
    private String username;

    @NotBlank
    private String email;

    @NotBlank
    private String password;

    private boolean admin;
}
