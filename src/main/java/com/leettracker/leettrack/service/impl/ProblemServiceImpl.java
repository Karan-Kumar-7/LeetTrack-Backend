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
import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import jakarta.servlet.http.HttpServletResponse;
import java.awt.Color;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.io.PrintWriter;

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
                problem.isFavorite(),
                problem.getNotes(),
                problem.getTimeComplexity(),
                problem.getSpaceComplexity(),
                problem.getProblemLink()
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
        problem.setNotes(request.getNotes());
        problem.setTimeComplexity(request.getTimeComplexity());
        problem.setSpaceComplexity(request.getSpaceComplexity());
        problem.setProblemLink(
                generateProblemLink(request.getTitle())
        );

        problemRepository.save(problem);
    }

    @Override
    public Page<ProblemResponse> getMyProblems(
            Pageable pageable,
            String title,
            String difficulty) {

        User user = getCurrentUser();

        Page<Problem> problems;

        boolean hasTitle =
                title != null && !title.trim().isEmpty();

        boolean hasDifficulty =
                difficulty != null &&
                        !difficulty.trim().isEmpty() &&
                        !difficulty.equalsIgnoreCase("ALL");

        if (hasTitle && hasDifficulty) {

            problems = problemRepository
                    .findByUserAndTitleContainingIgnoreCaseAndDifficulty(
                            user,
                            title,
                            difficulty,
                            pageable
                    );

        } else if (hasTitle) {

            problems = problemRepository
                    .findByUserAndTitleContainingIgnoreCase(
                            user,
                            title,
                            pageable
                    );

        } else if (hasDifficulty) {

            problems = problemRepository
                    .findByUserAndDifficulty(
                            user,
                            difficulty,
                            pageable
                    );

        } else {

            problems = problemRepository
                    .findByUser(user, pageable);

        }

        return problems.map(this::mapToResponse);
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
        problem.setNotes(request.getNotes());
        problem.setTimeComplexity(request.getTimeComplexity());
        problem.setSpaceComplexity(request.getSpaceComplexity());
        problem.setProblemLink(
                generateProblemLink(request.getTitle())
        );

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

    @Override
    public ProblemResponse getProblemByID(Long id) {

        User user = getCurrentUser();

        Problem problem = problemRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new ResourceNotFoundException("Problem not found"));

        return mapToResponse(problem);
    }

    private String generateProblemLink(String title) {

        return "https://leetcode.com/problems/"
                + title.toLowerCase()
                .replace(" ", "-")
                .replace(",", "")
                .replace("'", "")
                .replace("(", "")
                .replace(")", "")
                + "/";
    }

    @Override
    public void exportCsv(HttpServletResponse response) throws IOException {

        User user = getCurrentUser();

        List<Problem> problems = problemRepository.findByUser(user);

        response.setContentType("text/csv");
        response.setHeader(
                "Content-Disposition",
                "attachment; filename=leetcode-problems.csv"
        );

        PrintWriter writer = response.getWriter();

        writer.println("LeetCode ID,Title,Difficulty,Topic,Solved Date,Favorite,Time Complexity,Space Complexity,Notes");

        for (Problem problem : problems) {

            writer.printf(
                    "\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\"%n",
                    problem.getLeetcodeId(),
                    problem.getTitle(),
                    problem.getDifficulty(),
                    problem.getTopic(),
                    problem.getSolvedDate(),
                    problem.isFavorite() ? "Yes" : "No",
                    problem.getTimeComplexity() == null ? "" : problem.getTimeComplexity(),
                    problem.getSpaceComplexity() == null ? "" : problem.getSpaceComplexity(),
                    problem.getNotes() == null ? "" : problem.getNotes().replace("\"", "\"\"")
            );

        }

        writer.flush();

    }

    private void addTableHeader(PdfPTable table, String text) {

        PdfPCell cell = new PdfPCell();

        cell.setBackgroundColor(new Color(6, 182, 212));

        cell.setPadding(8);

        Font font = new Font(
                Font.HELVETICA,
                12,
                Font.BOLD,
                Color.WHITE
        );

        cell.setPhrase(new Phrase(text, font));

        table.addCell(cell);
    }

    @Override
    public void exportPdf(HttpServletResponse response) throws IOException {

        User user = getCurrentUser();

        List<Problem> problems = problemRepository.findByUser(user);

        response.setContentType("application/pdf");
        response.setHeader(
                "Content-Disposition",
                "attachment; filename=LeetTrack_Report.pdf"
        );

        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, response.getOutputStream());

        document.open();

        Font titleFont = new Font(Font.HELVETICA, 22, Font.BOLD);
        Font headingFont = new Font(Font.HELVETICA, 14, Font.BOLD);
        Font normalFont = new Font(Font.HELVETICA, 12);

        Paragraph title = new Paragraph("LeetTrack Report", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);

        document.add(title);
        document.add(new Paragraph(" "));

        document.add(new Paragraph(
                "Total Problems Solved : " + problemRepository.countByUser(user),
                headingFont
        ));

        document.add(new Paragraph(
                "Favorite Problems : " + problemRepository.countByUserAndFavoriteTrue(user),
                headingFont
        ));

        document.add(new Paragraph(" "));

        document.add(new Paragraph("User: " + user.getName(), headingFont));
        document.add(new Paragraph("Email: " + user.getEmail(), normalFont));
        document.add(new Paragraph(" "));

        PdfPTable table = new PdfPTable(6);

        table.setWidthPercentage(100);

        table.setSpacingBefore(15);

        table.setWidths(new float[]{
                1.2f,
                3.5f,
                2f,
                2f,
                2.5f,
                1.2f
        });

        addTableHeader(table, "ID");
        addTableHeader(table, "Title");
        addTableHeader(table, "Difficulty");
        addTableHeader(table, "Topic");
        addTableHeader(table, "Solved Date");
        addTableHeader(table, "★");

        for (Problem problem : problems) {

            table.addCell(String.valueOf(problem.getLeetcodeId()));

            table.addCell(problem.getTitle());

            table.addCell(problem.getDifficulty());

            table.addCell(problem.getTopic());

            table.addCell(problem.getSolvedDate().toString());

            table.addCell(problem.isFavorite() ? "⭐" : "");

        }

        document.add(table);

        document.close();

    }

    @Override
    public List<HeatmapResponse> getHeatmapData() {

        User user = getCurrentUser();

        List<Object[]> results = problemRepository.getHeatmap(user);

        List<HeatmapResponse> response = new ArrayList<>();

        for (Object[] row : results) {

            response.add(
                    new HeatmapResponse(
                            (LocalDate) row[0],
                            (Long) row[1]
                    )
            );

        }

        return response;

    }

}