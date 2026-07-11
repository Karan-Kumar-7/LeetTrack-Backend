package com.leettracker.leettrack.service.impl;

import com.leettracker.leettrack.dto.ProblemRequest;
import com.leettracker.leettrack.dto.StreakResponse;
import com.leettracker.leettrack.entity.Problem;
import com.leettracker.leettrack.entity.User;
import com.leettracker.leettrack.exception.ResourceNotFoundException;
import com.leettracker.leettrack.repository.ProblemRepository;
import com.leettracker.leettrack.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProblemServiceImplTest {

    @Mock
    private ProblemRepository problemRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ProblemServiceImpl problemService;

    @BeforeEach
    void setUp() {

        SecurityContext context = SecurityContextHolder.createEmptyContext();

        Authentication authentication = mock(Authentication.class);

        when(authentication.getName()).thenReturn("karan@example.com");

        context.setAuthentication(authentication);

        SecurityContextHolder.setContext(context);
    }

    @Test
    void shouldToggleFavorite() {

        // Arrange
        User user = new User();
        user.setEmail("karan@example.com");

        Problem problem = new Problem();
        problem.setFavorite(false);

        when(userRepository.findByEmail("karan@example.com"))
                .thenReturn(Optional.of(user));

        when(problemRepository.findByIdAndUser(1L, user))
                .thenReturn(Optional.of(problem));

        boolean result = problemService.toggleFavorite(1L);

        assertTrue(result);
        assertTrue(problem.isFavorite());

        verify(problemRepository).save(problem);
    }

    @Test
    void shouldUnfavoriteProblem() {

        User user = new User();
        user.setEmail("karan@example.com");

        Problem problem = new Problem();
        problem.setFavorite(true);

        when(userRepository.findByEmail("karan@example.com"))
                .thenReturn(Optional.of(user));

        when(problemRepository.findByIdAndUser(1L, user))
                .thenReturn(Optional.of(problem));

        boolean result = problemService.toggleFavorite(1L);

        assertFalse(result);
        assertFalse(problem.isFavorite());

        verify(problemRepository).save(problem);
    }

    @Test
    void shouldThrowResourceNotFoundExceptionWhenProblemDoesNotExist() {

        User user = new User();
        user.setEmail("karan@example.com");

        when(userRepository.findByEmail("karan@example.com"))
                .thenReturn(Optional.of(user));

        when(problemRepository.findByIdAndUser(1L, user))
                .thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> problemService.toggleFavorite(1L)
        );

        assertEquals("Problem not found", exception.getMessage());

        verify(problemRepository, never()).save(any(Problem.class));
    }

    @Test
    void shouldReturnZeroStreakWhenUserHasNoProblems() {

        User user = new User();
        user.setEmail("karan@example.com");

        when(userRepository.findByEmail("karan@example.com"))
                .thenReturn(Optional.of(user));

        when(problemRepository.findByUserOrderBySolvedDateAsc(user))
                .thenReturn(Collections.emptyList());

        StreakResponse response = problemService.getStreak();

        assertEquals(0, response.getCurrentStreak());
        assertEquals(0, response.getLongestStreak());

        verify(problemRepository).findByUserOrderBySolvedDateAsc(user);
    }

    @Test
    void shouldReturnOneDayStreak() {

        User user = new User();
        user.setEmail("karan@example.com");

        Problem p = new Problem();
        p.setSolvedDate(LocalDate.now());

        when(userRepository.findByEmail("karan@example.com"))
                .thenReturn(Optional.of(user));

        when(problemRepository.findByUserOrderBySolvedDateAsc(user))
                .thenReturn(List.of(p));

        StreakResponse response = problemService.getStreak();

        assertEquals(1, response.getCurrentStreak());
        assertEquals(1, response.getLongestStreak());
    }

    @Test
    void shouldReturnThreeDayCurrentAndLongestStreak() {

        User user = new User();
        user.setEmail("karan@example.com");

        Problem p1 = new Problem();
        p1.setSolvedDate(LocalDate.now().minusDays(2));

        Problem p2 = new Problem();
        p2.setSolvedDate(LocalDate.now().minusDays(1));

        Problem p3 = new Problem();
        p3.setSolvedDate(LocalDate.now());

        when(userRepository.findByEmail("karan@example.com"))
                .thenReturn(Optional.of(user));

        when(problemRepository.findByUserOrderBySolvedDateAsc(user))
                .thenReturn(List.of(p1, p2, p3));

        StreakResponse response = problemService.getStreak();

        assertEquals(3, response.getCurrentStreak());
        assertEquals(3, response.getLongestStreak());
    }

    @Test
    void shouldIgnoreDuplicateSolvedDates() {

        User user = new User();
        user.setEmail("karan@example.com");

        Problem p1 = new Problem();
        p1.setSolvedDate(LocalDate.now().minusDays(2));

        Problem p2 = new Problem();
        p2.setSolvedDate(LocalDate.now().minusDays(2));

        Problem p3 = new Problem();
        p3.setSolvedDate(LocalDate.now().minusDays(1));

        Problem p4 = new Problem();
        p4.setSolvedDate(LocalDate.now());

        when(userRepository.findByEmail("karan@example.com"))
                .thenReturn(Optional.of(user));

        when(problemRepository.findByUserOrderBySolvedDateAsc(user))
                .thenReturn(List.of(p1, p2, p3, p4));

        StreakResponse response = problemService.getStreak();

        assertEquals(3, response.getCurrentStreak());
        assertEquals(3, response.getLongestStreak());
    }

    @Test
    void shouldResetCurrentStreakWhenConsecutiveDaysBreak() {

        User user = new User();
        user.setEmail("karan@example.com");

        Problem p1 = new Problem();
        p1.setSolvedDate(LocalDate.now().minusDays(5));

        Problem p2 = new Problem();
        p2.setSolvedDate(LocalDate.now().minusDays(4));

        Problem p3 = new Problem();
        p3.setSolvedDate(LocalDate.now());

        when(userRepository.findByEmail("karan@example.com"))
                .thenReturn(Optional.of(user));

        when(problemRepository.findByUserOrderBySolvedDateAsc(user))
                .thenReturn(List.of(p1, p2, p3));

        StreakResponse response = problemService.getStreak();

        assertEquals(1, response.getCurrentStreak());
        assertEquals(2, response.getLongestStreak());
    }

    @Test
    void shouldReturnZeroCurrentStreakWhenLastSolvedDateIsOld() {

        User user = new User();
        user.setEmail("karan@example.com");

        Problem p1 = new Problem();
        p1.setSolvedDate(LocalDate.now().minusDays(6));

        Problem p2 = new Problem();
        p2.setSolvedDate(LocalDate.now().minusDays(5));

        when(userRepository.findByEmail("karan@example.com"))
                .thenReturn(Optional.of(user));

        when(problemRepository.findByUserOrderBySolvedDateAsc(user))
                .thenReturn(List.of(p1, p2));

        StreakResponse response = problemService.getStreak();

        assertEquals(0, response.getCurrentStreak());
        assertEquals(2, response.getLongestStreak());
    }

    @Test
    void shouldThrowExceptionWhenUserDoesNotExist() {

        when(userRepository.findByEmail("karan@example.com"))
                .thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> problemService.getStreak()
        );

        assertEquals("User not found", exception.getMessage());
    }

    @Test
    void saveProblemSuccessfully() {

        User user = new User();
        user.setEmail("karan@example.com");

        ProblemRequest request = new ProblemRequest();
        request.setLeetcodeId(1);
        request.setTitle("Two Sum");
        request.setDifficulty("Easy");
        request.setTopic("Array");
        request.setSolvedDate(LocalDate.now());
        request.setFavorite(true);

        when(userRepository.findByEmail("karan@example.com"))
                .thenReturn(Optional.of(user));

        problemService.addProblem(request);

        ArgumentCaptor<Problem> problemCaptor =
                ArgumentCaptor.forClass(Problem.class);

        verify(problemRepository).save(problemCaptor.capture());

        Problem savedProblem = problemCaptor.getValue();

        assertEquals(request.getLeetcodeId(), savedProblem.getLeetcodeId());
        assertEquals(request.getTitle(), savedProblem.getTitle());
        assertEquals(request.getDifficulty(), savedProblem.getDifficulty());
        assertEquals(request.getTopic(), savedProblem.getTopic());
        assertEquals(request.getSolvedDate(), savedProblem.getSolvedDate());
        assertEquals(request.isFavorite(), savedProblem.isFavorite());
        assertEquals(user, savedProblem.getUser());
    }

    @Test
    void shouldUpdateProblemSuccessfully() {

        // Arrange
        User user = new User();
        user.setEmail("karan@example.com");

        Problem problem = new Problem();
        problem.setLeetcodeId(1);
        problem.setTitle("Old Title");
        problem.setDifficulty("Easy");
        problem.setTopic("Array");
        problem.setSolvedDate(LocalDate.now().minusDays(1));
        problem.setFavorite(false);
        problem.setUser(user);

        ProblemRequest request = new ProblemRequest();
        request.setLeetcodeId(2);
        request.setTitle("Valid Parentheses");
        request.setDifficulty("Medium");
        request.setTopic("Stack");
        request.setSolvedDate(LocalDate.now());
        request.setFavorite(true);

        when(userRepository.findByEmail("karan@example.com"))
                .thenReturn(Optional.of(user));

        when(problemRepository.findByIdAndUser(1L, user))
                .thenReturn(Optional.of(problem));

        problemService.updateProblem(1L, request);

        ArgumentCaptor<Problem> captor =
                ArgumentCaptor.forClass(Problem.class);

        verify(problemRepository).save(captor.capture());

        Problem updatedProblem = captor.getValue();

        assertEquals(request.getLeetcodeId(), updatedProblem.getLeetcodeId());
        assertEquals(request.getTitle(), updatedProblem.getTitle());
        assertEquals(request.getDifficulty(), updatedProblem.getDifficulty());
        assertEquals(request.getTopic(), updatedProblem.getTopic());
        assertEquals(request.getSolvedDate(), updatedProblem.getSolvedDate());
        assertEquals(request.isFavorite(), updatedProblem.isFavorite());

        // User should remain the same
        assertEquals(user, updatedProblem.getUser());
    }

    @Test
    void shouldThrowResourceNotFoundExceptionWhenProblemDoesNotExistWhenUpdating() {

        // Arrange
        User user = new User();
        user.setEmail("karan@example.com");

        ProblemRequest request = new ProblemRequest();

        when(userRepository.findByEmail("karan@example.com"))
                .thenReturn(Optional.of(user));

        when(problemRepository.findByIdAndUser(1L, user))
                .thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> problemService.updateProblem(1L, request)
        );

        assertEquals("Problem not found", exception.getMessage());

        verify(problemRepository, never()).save(any(Problem.class));
    }

    @Test
    void shouldDeleteProblemSuccessfully() {

        // Arrange
        User user = new User();
        user.setEmail("karan@example.com");

        Problem problem = new Problem();

        when(userRepository.findByEmail("karan@example.com"))
                .thenReturn(Optional.of(user));

        when(problemRepository.findByIdAndUser(1L, user))
                .thenReturn(Optional.of(problem));

        // Act
        problemService.deleteProblem(1L);

        // Assert
        verify(problemRepository).delete(problem);
    }

}
