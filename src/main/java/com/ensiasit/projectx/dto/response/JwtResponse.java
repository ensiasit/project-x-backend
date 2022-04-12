package com.ensiasit.projectx.dto.response;

import com.ensiasit.projectx.utils.RoleEnum;
import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class JwtResponse {

    private String accessToken;
    @Builder.Default
    private String tokenType = "Bearer";
    private Long id;
    private String username;
    private String email;
    private Set<RoleEnum> roles;
}
