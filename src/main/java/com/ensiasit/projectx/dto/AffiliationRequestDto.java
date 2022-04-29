package com.ensiasit.projectx.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class AffiliationRequestDto {
    private long id;

    @NotBlank
    private String name;

    @NotBlank
    private String country;

    private MultipartFile logo;
}
