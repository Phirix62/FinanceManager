package com.FinFlow.FinanceManager.controller;

import com.FinFlow.FinanceManager.dto.GraphDTO;
import com.FinFlow.FinanceManager.dto.StatsDTO;
import com.FinFlow.FinanceManager.services.stats.StatsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;



class StatsControllerTest {

    private StatsService statsService;
    private StatsController statsController;

    @BeforeEach
    void setUp() {
        statsService = mock(StatsService.class);
        statsController = new StatsController(statsService);
    }

    @Test
    void getChartDetails_ReturnsGraphDTO() {
        Long userId = 1L;
        GraphDTO mockGraphDTO = new GraphDTO();
        when(statsService.getChartData(userId)).thenReturn(mockGraphDTO);

        ResponseEntity<GraphDTO> response = statsController.getChartDetails(userId);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(mockGraphDTO, response.getBody());
        verify(statsService, times(1)).getChartData(userId);
    }

    @Test
    void getStats_ReturnsStatsObject() {
        Long userId = 2L;
        // Replace Object with StatsDTO or the actual return type of getStats
        StatsDTO mockStats = new StatsDTO();
        when(statsService.getStats(userId)).thenReturn(mockStats);

        ResponseEntity<?> response = statsController.getStats(userId);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(mockStats, response.getBody());
        verify(statsService, times(1)).getStats(userId);
    }
}
