package com.ensiasit.projectx.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class AffiliationRequest {
    private long id;

    @NotBlank
    private String name;

    @NotBlank
    private String country;

    @NotNull
    private MultipartFile logo;
}
