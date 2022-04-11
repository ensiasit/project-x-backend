package com.ensiasit.projectx.models;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Table(name = "contest",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "name")
        })
public class Contest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private LocalDateTime freezeTime;
    private LocalDateTime unfreezeTime;
    @Column(nullable = false)
    private Boolean publicScoreboard;

}
