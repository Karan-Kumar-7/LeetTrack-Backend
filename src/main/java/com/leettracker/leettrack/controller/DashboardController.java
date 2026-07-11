package com.leettracker.leettrack.controller;

import com.leettracker.leettrack.dto.DashboardResponse;
import com.leettracker.leettrack.service.ProblemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Dashboard", description = "Dashboard statistics and analytics")
public class DashboardController {

    private final ProblemService problemService;
    @Operation(
            summary = "Get dashboard statistics",
            description = "Returns total solved problems, difficulty counts, and favorite count."
    )
    @GetMapping
    public DashboardResponse getDashboard() {
        return problemService.getDashboard();
    }
}