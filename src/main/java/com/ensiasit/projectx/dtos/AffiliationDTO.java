package com.ensiasit.projectx.dtos;


import com.sun.istack.NotNull;
import lombok.NonNull;

import javax.persistence.Column;
import javax.persistence.Id;

public class AffiliationDTO {

    @NotNull
    private Long id;

    @NotNull
    private String name;

    @NotNull
    private String country;

    @NotNull
    private String logo;

}
