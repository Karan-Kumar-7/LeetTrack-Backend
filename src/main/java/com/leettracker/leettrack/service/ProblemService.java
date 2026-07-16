package com.leettracker.leettrack.service;

import com.leettracker.leettrack.dto.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProblemService {

    void addProblem(ProblemRequest request);

    void updateProblem(Long id, ProblemRequest request);

    Page<ProblemResponse> getMyProblems(
            Pageable pageable,
            String title,
            String difficulty
    );

    DashboardResponse getDashboard();

    void deleteProblem(Long id);

    List<ProblemResponse> searchProblems(String title);

    List<ProblemResponse> favoriteProblems();

    List<ProblemResponse> difficultProblems(String difficulty);

    boolean toggleFavorite(Long id);

    List<ProblemResponse> byDate(LocalDate start, LocalDate end);

    StreakResponse getStreak();

    List<TopicStatsResponse> groupByTopic();

    List<MonthlyActivityResponse> getMonthlyActivity();

    DifficultyDistributionResponse getDifficultyDistribution();

    ProblemResponse getProblemByID(Long id);

    void exportCsv(HttpServletResponse response) throws IOException;

    void exportPdf(HttpServletResponse response) throws IOException;

    //ProfileResponse getProfile();

    List<HeatmapResponse> getHeatmapData();

}