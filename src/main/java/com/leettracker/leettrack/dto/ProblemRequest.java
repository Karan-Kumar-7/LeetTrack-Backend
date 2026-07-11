package com.leettracker.leettrack.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProblemRequest {

    @NotNull(message = "LeetCode ID is required")
    private Integer leetcodeId;

    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Difficulty is required")
    private String difficulty;

    @NotBlank(message = "Topic is required")
    private String topic;

    @NotNull(message = "Solved date is required")
    private LocalDate solvedDate;

    private boolean favorite;
}