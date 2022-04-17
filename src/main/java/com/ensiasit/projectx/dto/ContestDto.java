package com.ensiasit.projectx.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ContestDto {
    private long id;

    @NotBlank
    private String name;

    @NotNull
    @Future
    private LocalDateTime startTime;

    @NotNull
    @Future
    private LocalDateTime endTime;

    @NotNull
    @Future
    private LocalDateTime freezeTime;

    @NotNull
    @Future
    private LocalDateTime unfreezeTime;

    private boolean publicScoreboard;
}
