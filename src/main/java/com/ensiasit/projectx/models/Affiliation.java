package com.ensiasit.projectx.models;


import com.sun.istack.NotNull;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@NoArgsConstructor
@Entity
public class Affiliation {

    @Id
    private Long id;

    @NotNull
    @Column
    private String name;

    @NotNull
    @Column
    private String country;

    @NotNull
    @Column
    private String logo;

}
