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
public class TeamDto {
    private long id;

    @NotBlank
    private String name;

    private long affiliationId;
}
