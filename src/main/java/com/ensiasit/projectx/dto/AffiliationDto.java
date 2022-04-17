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
public class AffiliationDto {
    private long id;

    @NotBlank
    private String name;

    @NotBlank
    private String country;

    @NotBlank
    private String logo;
}
