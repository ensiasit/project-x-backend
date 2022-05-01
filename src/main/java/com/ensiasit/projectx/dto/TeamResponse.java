package com.ensiasit.projectx.dto;

import com.ensiasit.projectx.models.Affiliation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class TeamResponse {
    private long id;

    @NotBlank
    private String name;

    private Affiliation affiliation;

    private ContestDto contest;

    private Set<String> membersEmails;
}
