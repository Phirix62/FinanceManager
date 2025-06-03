package com.FinFlow.FinanceManager.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.FinFlow.FinanceManager.dto.GraphDTO;
import com.FinFlow.FinanceManager.services.stats.StatsService;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Controller for handling statistics-related endpoints for a specific user.
 * <p>
 * Provides endpoints to retrieve chart data and general statistics for a user.
 * </p>
 *
 * <p>
 * All endpoints are prefixed with <code>/api/stats/{userId}</code>.
 * </p>
 *
 * @author YourName
 */
@RestController
@RequestMapping("/api/stats/{userId}")
@RequiredArgsConstructor
@CrossOrigin("*")
public class StatsController {

    private static final Logger logger = LoggerFactory.getLogger(StatsController.class);

    private final StatsService statsService;

    /**
     * Retrieves chart data for the specified user.
     *
     * @param userId the ID of the user whose chart data is to be retrieved
     * @return a {@link ResponseEntity} containing the {@link GraphDTO} with chart details
     */
    @GetMapping("/chart")
    public ResponseEntity<GraphDTO> getChartDetails(@PathVariable Long userId){
        // Log the request for chart details
        logger.info("Fetching chart details for userId: {}", userId);
        return ResponseEntity.ok(statsService.getChartData(userId));
    }

    /**
     * Retrieves general statistics for the specified user.
     *
     * @param userId the ID of the user whose statistics are to be retrieved
     * @return a {@link ResponseEntity} containing the statistics data
     */
    @GetMapping
    public ResponseEntity<?> getStats(@PathVariable Long userId){
        // Log the request for general statistics
        logger.info("Fetching statistics for userId: {}", userId);
        return ResponseEntity.ok(statsService.getStats(userId));
    }

}
