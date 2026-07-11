package com.leettracker.leettrack.controller;

import com.leettracker.leettrack.dto.StreakResponse;
import com.leettracker.leettrack.security.jwt.JwtAuthenticationFilter;
import com.leettracker.leettrack.service.ProblemService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(ProblemController.class)
@AutoConfigureMockMvc(addFilters = false)
class ProblemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProblemService problemService;

    @MockitoBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

        @Test
        @WithMockUser
        void shouldToggleFavorite() throws Exception {

            when(problemService.toggleFavorite(1L))
                    .thenReturn(true);

            mockMvc.perform(
                            patch("/api/problems/1/favorite")
                    )
                    .andExpect(status().isOk())
                    .andExpect(content().string("true"));

            verify(problemService).toggleFavorite(1L);
        }

    @Test
    @WithMockUser
    void shouldReturnStreak() throws Exception {

        // Arrange
        StreakResponse streak = new StreakResponse(5, 10);

        when(problemService.getStreak())
                .thenReturn(streak);

        // Act & Assert
        mockMvc.perform(get("/api/problems/streak"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.currentStreak").value(5))
                .andExpect(jsonPath("$.longestStreak").value(10));

        verify(problemService).getStreak();
    }



}