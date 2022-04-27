package com.ensiasit.projectx.models;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "affiliations")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Affiliation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String country;

    @Lob
    private byte[] logo;
}
