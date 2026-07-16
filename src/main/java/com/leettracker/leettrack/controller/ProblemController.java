package com.leettracker.leettrack.controller;

import com.leettracker.leettrack.dto.*;
import com.leettracker.leettrack.service.ProblemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestParam;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/problems")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Problems", description = "Manage solved LeetCode problems")
public class ProblemController {

    private final ProblemService problemService;

    @Operation(
            summary = "Add a solved problem",
            description = "Adds a solved LeetCode problem for the authenticated user."
    )
    @PostMapping
    public String addProblem(@Valid @RequestBody ProblemRequest request) {

        problemService.addProblem(request);

        return "Problem added successfully!";
    }

    @Operation(
            summary = "Update a problem",
            description = "Updates an existing solved problem owned by the authenticated user."
    )
    @PutMapping("/{id}")
    public String updateProblem(@PathVariable Long id,
                                @Valid @RequestBody ProblemRequest request) {

        problemService.updateProblem(id, request);

        return "Problem updated successfully!";
    }

    @Operation(
            summary = "Get all solved problems",
            description = "Returns all solved problems belonging to the authenticated user."
    )
    @GetMapping
    public Page<ProblemResponse> getMyProblems(
            Pageable pageable,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String difficulty) {

        return problemService.getMyProblems(pageable, title, difficulty);
    }

    @Operation(
            summary = "Delete a problem",
            description = "Deletes a solved problem owned by the authenticated user."
    )
    @DeleteMapping("/{id}")
    public String deleteProblem(@PathVariable Long id) {

        problemService.deleteProblem(id);

        return "Problem deleted successfully";
    }

    @Operation(
            summary = "Search by title",
            description = "Search solved problems by title."
    )
    @GetMapping("/search")
    public List<ProblemResponse> searchProblems(@RequestParam String title) {
        return problemService.searchProblems(title);
    }

    @Operation(
            summary = "View favorite problems",
            description = "Returns all problems marked as favorite."
    )
    @GetMapping("/favorites")
    public List<ProblemResponse> favoriteProblems() {

        return problemService.favoriteProblems();
    }

    @Operation(
            summary = "Filter by difficulty",
            description = "Returns solved problems filtered by difficulty level."
    )
    @GetMapping("/filter")
    public List<ProblemResponse> difficultProblems(@RequestParam String difficulty) {
        return problemService.difficultProblems(difficulty);
    }

    @Operation(
            summary = "Toggle favorite.",
            description = "Toggle any problem as favorite and not favorite."
    )
    @PatchMapping("/{id}/favorite")
    public boolean toggleFavorite(@PathVariable Long id) {

        return problemService.toggleFavorite(id);
    }

    @Operation(
            summary = "View problems solved between dates.",
            description = "Returns all the problems solved between two dates in sorted order."
    )
    @GetMapping("/searchByDate")
    public List<ProblemResponse> byDate(@RequestParam LocalDate start, @RequestParam LocalDate end) {

        return problemService.byDate(start, end);
    }

    @Operation(
            summary = "View your streaks.",
            description = "Returns your current streak and longest streak."
    )
    @GetMapping("/streak")
    public StreakResponse getStreak() {

        return problemService.getStreak();
    }

    @Operation(
            summary = "Group by topic.",
            description = "Returns your problems solved on each topic."
    )
    @GetMapping("/byTopic")
    public List<TopicStatsResponse> groupByTopic() {

        return problemService.groupByTopic();
    }

    @Operation(
            summary = "Monthly activity",
            description = "View the number of problems solved every month."
    )
    @GetMapping("/monthly-activity")
    public List<MonthlyActivityResponse> getMonthlyActivity() {

        return problemService.getMonthlyActivity();
    }

    @Operation(
            summary = "Difficulty Distribution",
            description = "View the number of problems solved for each difficulty level."
    )
    @GetMapping("/difficultyDistribution")
    public DifficultyDistributionResponse getDifficultyDistribution() {

        return problemService.getDifficultyDistribution();
    }

    @Operation(
            summary = "Get problem by id",
            description = "Returns a single solved problem."
    )
    @GetMapping("/{id}")
    public ProblemResponse getProblemByID(@PathVariable Long id) {

        return problemService.getProblemByID(id);

    }

    @GetMapping("/export")
    public void exportProblems(HttpServletResponse response) throws IOException {

        problemService.exportCsv(response);

    }

    @GetMapping("/export/pdf")
    public void exportPdf(HttpServletResponse response) throws IOException {

        problemService.exportPdf(response);

    }

    @GetMapping("/heatmap")
    public List<HeatmapResponse> getHeatmap() {

        return problemService.getHeatmapData();

    }

//    @GetMapping("/profile")
//    public ProfileResponse getProfile() {
//        return problemService.getProfile();
//    }

}