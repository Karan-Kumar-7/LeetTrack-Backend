package com.leettracker.leettrack.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "problems")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Problem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer leetcodeId;

    private String title;

    private String difficulty;

    private String topic;

    private LocalDate solvedDate;

    private boolean favorite;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(length = 3000)
    private String notes;

    private String timeComplexity;

    private String spaceComplexity;

    private String problemLink;
}