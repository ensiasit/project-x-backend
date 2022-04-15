package com.ensiasit.projectx.models;


import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@AllArgsConstructor
@Entity
public class Submission {

    @Id
    private Long id;

    @Column
    private String language;

    @Column
    private 

}
