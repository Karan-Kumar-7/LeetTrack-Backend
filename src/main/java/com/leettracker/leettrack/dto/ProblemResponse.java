package com.leettracker.leettrack.dto;

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
}