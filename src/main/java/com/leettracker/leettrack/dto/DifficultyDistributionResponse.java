package com.leettracker.leettrack.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DifficultyDistributionResponse {

    private long easy;
    private long medium;
    private long hard;
}
