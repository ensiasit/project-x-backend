package com.ensiasit.projectx.models;

import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "users")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(exclude = "teams")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    @Column(unique = true, length = 50)
    private String email;

    private String password;

    @ManyToMany(mappedBy = "members")
    private Set<Team> teams;
}