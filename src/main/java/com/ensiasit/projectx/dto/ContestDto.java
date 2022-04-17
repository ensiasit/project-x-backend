package com.ensiasit.projectx.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ContestDto {
    @NotBlank
    private long id;

    @NotBlank
    @NotNull
    private String name;

    @NotBlank
    private LocalDateTime startTime;

    @NotBlank
    private LocalDateTime endTime;

    @NotBlank
    private LocalDateTime freezeTime;

    @NotBlank
    private LocalDateTime unfreezeTime;

    @NotBlank
    private boolean publicScoreboard;
}
