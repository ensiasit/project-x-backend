package com.ensiasit.projectx.models;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "contests")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Contest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, length = 100)
    private String name;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private LocalDateTime freezeTime;

    private LocalDateTime unfreezeTime;

    @Column(nullable = false)
    private boolean publicScoreboard;
}
