package com.ensiasit.projectx.dto;

import com.ensiasit.projectx.utils.RoleEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class UserContestRoleDto {
    private RoleEnum role;

    private ContestDto contest;
}
