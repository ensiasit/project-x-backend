package com.ensiasit.projectx.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class JwtResponse {
    private String token;

    @Builder.Default
    private String tokenType = "Bearer";

    private Long id;

    private String username;

    private String email;
}
