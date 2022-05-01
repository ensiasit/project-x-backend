package com.ensiasit.projectx.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class MemberResponse {
    private long id;

    private String teamName;

    private Set<String> membersEmails;
}
