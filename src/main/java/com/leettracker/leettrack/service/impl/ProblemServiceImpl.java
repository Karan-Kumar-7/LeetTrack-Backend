package com.leettracker.leettrack.service.impl;

import com.leettracker.leettrack.dto.*;
import com.leettracker.leettrack.entity.Problem;
import com.leettracker.leettrack.entity.User;
import com.leettracker.leettrack.exception.ResourceNotFoundException;
import com.leettracker.leettrack.repository.ProblemRepository;
import com.leettracker.leettrack.repository.UserRepository;
import com.leettracker.leettrack.service.ProblemService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProblemServiceImpl implements ProblemService {

    private final ProblemRepository problemRepository;
    private final UserRepository userRepository;

    private User getCurrentUser() {

        String email = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    private ProblemResponse mapToResponse(Problem problem) {

        return new ProblemResponse(
                problem.getId(),
                problem.getLeetcodeId(),
                problem.getTitle(),
                problem.getDifficulty(),
                problem.getTopic(),
                problem.getSolvedDate(),
                problem.isFavorite()
        );
    }

    @Override
    public void addProblem(ProblemRequest request) {

        User user = getCurrentUser();

        Problem problem = new Problem();

        problem.setLeetcodeId(request.getLeetcodeId());
        problem.setTitle(request.getTitle());
        problem.setDifficulty(request.getDifficulty());
        problem.setTopic(request.getTopic());
        problem.setSolvedDate(request.getSolvedDate());
        problem.setFavorite(request.isFavorite());

        problem.setUser(user);

        problemRepository.save(problem);
    }

    @Override
    public Page<ProblemResponse> getMyProblems(Pageable pageable) {

        User user = getCurrentUser();

        Page<Problem> problems =
                problemRepository.findByUser(user, pageable);

        return problems.map(problem -> new ProblemResponse(
                problem.getId(),
                problem.getLeetcodeId(),
                problem.getTitle(),
                problem.getDifficulty(),
                problem.getTopic(),
                problem.getSolvedDate(),
                problem.isFavorite()
        ));
    }

    @Override
    public void updateProblem(Long id, ProblemRequest request) {

        User user = getCurrentUser();

        Problem problem = problemRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new ResourceNotFoundException("Problem not found"));

        problem.setLeetcodeId(request.getLeetcodeId());
        problem.setTitle(request.getTitle());
        problem.setDifficulty(request.getDifficulty());
        problem.setTopic(request.getTopic());
        problem.setSolvedDate(request.getSolvedDate());
        problem.setFavorite(request.isFavorite());

        problemRepository.save(problem);
    }

    @Override
    public DashboardResponse getDashboard() {

        User user = getCurrentUser();

        DashboardResponse response = new DashboardResponse();

        response.setTotalSolved(problemRepository.countByUser(user));
        response.setStreak(getStreak());
        response.setDifficultyDistribution(getDifficultyDistribution());
        response.setTopicStats(groupByTopic());
        response.setMonthlyActivity(getMonthlyActivity());
        response.setFavorites(problemRepository.findByUserAndFavoriteTrue(user)
                .stream()
                .map(this::mapToResponse)
                .toList()
        );
        response.setRecentFive(problemRepository.findTop5ByUserOrderBySolvedDateDesc(user)
                .stream()
                .map(this::mapToResponse)
                .toList()
        );

        return response;
    }

    @Override
    public void deleteProblem(Long id) {

        User user = getCurrentUser();

        Problem problem = problemRepository.findByIdAndUser(id,user)
                .orElseThrow(() -> new ResourceNotFoundException("Problem not found"));
        problemRepository.delete(problem);
    }

    @Override
    public List<ProblemResponse> searchProblems(String title) {

        User user = getCurrentUser();

        return problemRepository.findByUserAndTitleContainingIgnoreCase(user, title)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<ProblemResponse> favoriteProblems() {

        User user = getCurrentUser();

        return problemRepository.findByUserAndFavoriteTrue(user)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<ProblemResponse> difficultProblems(String difficulty) {

        User user = getCurrentUser();

        return problemRepository.findByUserAndDifficultyContainingIgnoreCase(user, difficulty)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public boolean toggleFavorite(Long id) {

        User user = getCurrentUser();

        Problem problem = problemRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new ResourceNotFoundException("Problem not found"));

        problem.setFavorite(!problem.isFavorite());

        problemRepository.save(problem);

        return problem.isFavorite();
    }

    @Override
    public List<ProblemResponse> byDate(LocalDate start, LocalDate end) {

        User user = getCurrentUser();

        return problemRepository.findByUserAndSolvedDateBetween(user, start, end)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public StreakResponse getStreak() {

        User user = getCurrentUser();

        List<Problem> problems =
                problemRepository.findByUserOrderBySolvedDateAsc(user);

        if (problems.isEmpty()) {
            return new StreakResponse(0, 0);
        }

        int current = 1;
        int longest = 1;

        LocalDate prev = problems.get(0).getSolvedDate();

        for (int i = 1; i < problems.size(); i++) {

            LocalDate curr = problems.get(i).getSolvedDate();

            if (prev.equals(curr)) continue;

            if (prev.plusDays(1).equals(curr)) {
                current++;
            } else {
                current = 1;
            }

            longest = Math.max(longest, current);

            prev = curr;
        }

        LocalDate today = LocalDate.now();
        LocalDate lastSolved = problems.get(problems.size() - 1).getSolvedDate();

        if (!lastSolved.plusDays(1).equals(today) && !lastSolved.equals(today)) {
            current = 0;
        }

        return new StreakResponse(current, longest);
    }

    @Override
    public List<TopicStatsResponse> groupByTopic() {

        User user = getCurrentUser();

        return problemRepository.getTopicStats(user);
    }

    @Override
    public List<MonthlyActivityResponse> getMonthlyActivity() {

        User user = getCurrentUser();

        return problemRepository.getMonthlyStats(user);
    }

    @Override
    public DifficultyDistributionResponse getDifficultyDistribution() {

        User user = getCurrentUser();

        return problemRepository.getDifficultyStats(user);
    }

}