package com.ensiasit.projectx.models;

import com.ensiasit.projectx.utils.RoleEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "user_contest_role")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class UserContestRole {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column
    private RoleEnum role;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @OneToOne
    @JoinColumn(name = "contest_id", referencedColumnName = "id")
    private Contest contest;
}