package com.ensiasit.projectx.models;

import com.ensiasit.projectx.utils.RoleEnum;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Table(name = "auth_roles")
@Data
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column
    private RoleEnum name;

    public Role(RoleEnum name) {
        this.name = name;
    }
}