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

@RestController
@RequestMapping("/api/stats/{userId}")
@RequiredArgsConstructor
@CrossOrigin("*")
public class StatsController {

    private final StatsService statsService;

    @GetMapping("/chart")
    public ResponseEntity<GraphDTO> getChartDetails(@PathVariable Long userId){
        return ResponseEntity.ok(statsService.getChartData(userId));
    }

    @GetMapping
    public ResponseEntity<?> getStats(@PathVariable Long userId){
        return ResponseEntity.ok(statsService.getStats(userId));
    }

}
