package com.leettracker.leettrack.dto;

import com.leettracker.leettrack.entity.Problem;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DashboardResponse {

        private long totalSolved;

        private StreakResponse streak;

        private DifficultyDistributionResponse difficultyDistribution;

        private List<TopicStatsResponse> topicStats;

        private List<MonthlyActivityResponse> monthlyActivity;

        private List<ProblemResponse> favorites;

        private List<ProblemResponse> recentFive;
}