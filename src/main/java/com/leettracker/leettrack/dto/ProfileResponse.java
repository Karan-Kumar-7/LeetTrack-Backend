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
public class ProfileResponse {

    private String name;
    private String email;
    private LocalDate joinedDate;

    private long totalSolved;
    private long favorites;

    private int currentStreak;
    private int longestStreak;
}
