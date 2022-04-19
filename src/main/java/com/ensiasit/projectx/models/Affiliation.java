package com.ensiasit.projectx.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "affiliations")
@NoArgsConstructor
@AllArgsConstructor
@Data
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
