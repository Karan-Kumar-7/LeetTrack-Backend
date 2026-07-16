package com.leettracker.leettrack.dto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProblemResponse {

    private Long id;
    private Integer leetcodeId;
    private String title;
    private String difficulty;
    private String topic;
    private LocalDate solvedDate;
    private boolean favorite;
    @Column(length = 3000)
    private String notes;
    private String timeComplexity;
    private String spaceComplexity;
    private String problemLink;
}