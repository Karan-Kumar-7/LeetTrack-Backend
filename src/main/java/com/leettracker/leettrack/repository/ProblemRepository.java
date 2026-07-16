package com.leettracker.leettrack.repository;

import com.leettracker.leettrack.dto.DifficultyDistributionResponse;
import com.leettracker.leettrack.dto.MonthlyActivityResponse;
import com.leettracker.leettrack.dto.TopicStatsResponse;
import com.leettracker.leettrack.entity.Problem;
import com.leettracker.leettrack.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProblemRepository extends JpaRepository<Problem, Long> {

    Optional<Problem> findByIdAndUser(Long id, User user);

    long countByUser(User user);

    List<Problem> findByUser(User user);

    List<Problem> findByUserAndTitleContainingIgnoreCase(User user, String title);

    List<Problem> findByUserAndFavoriteTrue(User user);

    List<Problem> findByUserAndDifficultyContainingIgnoreCase(User user, String difficulty);

    Page<Problem> findByUser(User user, Pageable pageable);

    List<Problem> findByUserAndSolvedDateBetween(User user, LocalDate start, LocalDate end);

    List<Problem> findByUserOrderBySolvedDateAsc(User user);

    @Query("""
    SELECT new com.leettracker.leettrack.dto.TopicStatsResponse(
        p.topic,
        COUNT(p)
    )
    FROM Problem p
    WHERE p.user = :user
    GROUP BY p.topic
    ORDER BY COUNT(p) DESC
""")
    List<TopicStatsResponse> getTopicStats(@org.springframework.data.repository.query.Param("user") User user);

    @Query("""
    SELECT new com.leettracker.leettrack.dto.MonthlyActivityResponse(
        MONTH(p.solvedDate),
        COUNT(p)
    )
    FROM Problem p
    WHERE p.user = :user
    GROUP BY MONTH(p.solvedDate)
    ORDER BY MONTH(p.solvedDate) ASC
""")
    List<MonthlyActivityResponse> getMonthlyStats(@org.springframework.data.repository.query.Param("user") User user);

    @Query("""
    SELECT new com.leettracker.leettrack.dto.DifficultyDistributionResponse(
        COALESCE(SUM(CASE WHEN p.difficulty = 'Easy' THEN 1 ELSE 0 END), 0),
        COALESCE(SUM(CASE WHEN p.difficulty = 'Medium' THEN 1 ELSE 0 END), 0),
        COALESCE(SUM(CASE WHEN p.difficulty = 'Hard' THEN 1 ELSE 0 END), 0)
    )
    FROM Problem p
    WHERE p.user = :user
""")
    DifficultyDistributionResponse getDifficultyStats(@org.springframework.data.repository.query.Param("user") User user);

    List<Problem> findTop5ByUserOrderBySolvedDateDesc(User user);

    Page<Problem> findByUserAndTitleContainingIgnoreCase(
            User user,
            String title,
            Pageable pageable
    );

    Page<Problem> findByUserAndDifficulty(
            User user,
            String difficulty,
            Pageable pageable
    );

    Page<Problem> findByUserAndTitleContainingIgnoreCaseAndDifficulty(
            User user,
            String title,
            String difficulty,
            Pageable pageable
    );

    String countByUserAndFavoriteTrue(User user);

    @Query("""
SELECT p.solvedDate, COUNT(p)
FROM Problem p
WHERE p.user = :user
GROUP BY p.solvedDate
ORDER BY p.solvedDate
""")
    List<Object[]> getHeatmap(User user);
}