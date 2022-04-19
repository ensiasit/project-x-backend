package com.ensiasit.projectx.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class AffiliationResponse {
    private long id;

    @NotBlank
    private String name;

    @NotBlank
    private String country;

    @NotNull
    private byte[] logo;
}
