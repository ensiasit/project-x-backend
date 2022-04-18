package com.ensiasit.projectx.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "teams")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Team {
    @OneToMany
    @JoinColumn(name = "team_id", referencedColumnName = "id")
    Set<User> members;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, length = 50)
    private String name;
    @OneToOne
    @JoinColumn(name = "contest_id", referencedColumnName = "id")
    private Contest contest;
    @OneToOne
    @JoinColumn(name = "affiliation_id", referencedColumnName = "id")
    private Affiliation affiliation;
}
