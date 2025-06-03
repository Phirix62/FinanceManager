package com.FinFlow.FinanceManager.services.stats;

import com.FinFlow.FinanceManager.dto.GraphDTO;
import com.FinFlow.FinanceManager.dto.StatsDTO;

public interface StatsService {

    GraphDTO getChartData(Long userId);

    StatsDTO getStats(Long userId);

}
