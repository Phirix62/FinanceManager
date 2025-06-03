package com.FinFlow.FinanceManager.services.stats;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.FinFlow.FinanceManager.dto.GraphDTO;
import com.FinFlow.FinanceManager.dto.StatsDTO;
import com.FinFlow.FinanceManager.entity.Expense;
import com.FinFlow.FinanceManager.entity.Income;
import com.FinFlow.FinanceManager.repository.ExpenseRepository;
import com.FinFlow.FinanceManager.repository.IncomeRepository;

import lombok.RequiredArgsConstructor;

/**
 * Service implementation for statistics-related operations.
 */
@Service
@RequiredArgsConstructor
public class StatsServiceImpl implements StatsService {

    private static final Logger logger = LoggerFactory.getLogger(StatsServiceImpl.class);

    private final IncomeRepository incomeRepository;
    private final ExpenseRepository expenseRepository;

    /**
     * Retrieves chart data (income and expenses) for the last 28 days for a user.
     *
     * @param userId the ID of the user
     * @return a GraphDTO containing lists of expenses and incomes
     */
    public GraphDTO getChartData(Long userId) {
        logger.info("Fetching chart data for userId: {}", userId);
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(27);

        GraphDTO graphDTO = new GraphDTO();
        graphDTO.setExpenseList(expenseRepository.findByUserIdAndDateBetween(userId, startDate, endDate));
        graphDTO.setIncomeList(incomeRepository.findByUserIdAndDateBetween(userId, startDate, endDate));
        logger.debug("Chart data fetched for userId: {} from {} to {}", userId, startDate, endDate);
        return graphDTO;
    }

    /**
     * Retrieves statistical data for a user, including totals, latest entries, balance, and min/max values.
     *
     * @param userId the ID of the user
     * @return a StatsDTO containing statistical information
     */
    public StatsDTO getStats(Long userId) {
        logger.info("Fetching statistics for userId: {}", userId);
        Double totalIncome = incomeRepository.sumAllAmountsByUserId(userId);
        Double totalExpense = expenseRepository.sumAllAmountsByUserId(userId);

        Optional<Income> optionalIncome = incomeRepository.findFirstByUserIdOrderByDateDesc(userId);
        Optional<Expense> optionalExpense = expenseRepository.findFirstByUserIdOrderByDateDesc(userId);

        StatsDTO statsDTO = new StatsDTO();
        statsDTO.setExpense(totalExpense);
        statsDTO.setIncome(totalIncome);

        optionalIncome.ifPresent(statsDTO::setLatestIncome);
        optionalExpense.ifPresent(statsDTO::setLatestExpense);

        statsDTO.setBalance(totalIncome - totalExpense);

        logger.debug("Total income: {}, Total expense: {}, Balance: {}", totalIncome, totalExpense, statsDTO.getBalance());

        List<Income> incomeList = incomeRepository.findAll();
        List<Expense> expenseList = expenseRepository.findAll();

        OptionalDouble minIncome = incomeList.stream().mapToDouble(Income::getAmount).min();
        OptionalDouble maxIncome = incomeList.stream().mapToDouble(Income::getAmount).max();
        OptionalDouble minExpense = expenseList.stream().mapToDouble(Expense::getAmount).min();
        OptionalDouble maxExpense = expenseList.stream().mapToDouble(Expense::getAmount).max();

        statsDTO.setMaxExpense(maxExpense.isPresent() ? maxExpense.getAsDouble() : null);
        statsDTO.setMinExpense(minExpense.isPresent() ? minExpense.getAsDouble() : null);
        statsDTO.setMaxIncome(maxIncome.isPresent() ? maxIncome.getAsDouble() : null);
        statsDTO.setMinIncome(minIncome.isPresent() ? minIncome.getAsDouble() : null);

        logger.debug("Min/Max Income: {}/{}", statsDTO.getMinIncome(), statsDTO.getMaxIncome());
        logger.debug("Min/Max Expense: {}/{}", statsDTO.getMinExpense(), statsDTO.getMaxExpense());

        return statsDTO;
    }

}
