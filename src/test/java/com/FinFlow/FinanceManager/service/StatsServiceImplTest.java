package com.FinFlow.FinanceManager.service;

import com.FinFlow.FinanceManager.dto.GraphDTO;
import com.FinFlow.FinanceManager.dto.StatsDTO;
import com.FinFlow.FinanceManager.entity.Expense;
import com.FinFlow.FinanceManager.entity.Income;
import com.FinFlow.FinanceManager.repository.ExpenseRepository;
import com.FinFlow.FinanceManager.repository.IncomeRepository;
import com.FinFlow.FinanceManager.services.stats.StatsServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;




class StatsServiceImplTest {

    private IncomeRepository incomeRepository;
    private ExpenseRepository expenseRepository;
    private StatsServiceImpl statsService;

    @BeforeEach
    void setUp() {
        incomeRepository = mock(IncomeRepository.class);
        expenseRepository = mock(ExpenseRepository.class);
        statsService = new StatsServiceImpl(incomeRepository, expenseRepository);
    }

    @Test
    void testGetChartData_ReturnsGraphDTOWithCorrectLists() {
        Long userId = 1L;
        LocalDate now = LocalDate.now();
        LocalDate startDate = now.minusDays(27);

        List<Expense> expenses = Arrays.asList(new Expense(), new Expense());
        List<Income> incomes = Arrays.asList(new Income(), new Income());

        when(expenseRepository.findByUserIdAndDateBetween(eq(userId), any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(expenses);
        when(incomeRepository.findByUserIdAndDateBetween(eq(userId), any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(incomes);

        GraphDTO result = statsService.getChartData(userId);

        assertEquals(expenses, result.getExpenseList());
        assertEquals(incomes, result.getIncomeList());
    }

    @Test
    void testGetStats_ReturnsStatsDTOWithCorrectValues() {
        Long userId = 2L;
        double totalIncome = 1000.0;
        double totalExpense = 400.0;

        Income latestIncome = new Income();
        latestIncome.setAmount(500);
        Expense latestExpense = new Expense();
        latestExpense.setAmount(200);

        List<Income> incomeList = Arrays.asList(
                createIncome(100.0),
                createIncome(500.0),
                createIncome(400.0)
        );
        List<Expense> expenseList = Arrays.asList(
                createExpense(50.0),
                createExpense(200.0),
                createExpense(150.0)
        );

        when(incomeRepository.sumAllAmountsByUserId(userId)).thenReturn(totalIncome);
        when(expenseRepository.sumAllAmountsByUserId(userId)).thenReturn(totalExpense);
        when(incomeRepository.findFirstByUserIdOrderByDateDesc(userId)).thenReturn(Optional.of(latestIncome));
        when(expenseRepository.findFirstByUserIdOrderByDateDesc(userId)).thenReturn(Optional.of(latestExpense));
        when(incomeRepository.findAll()).thenReturn(incomeList);
        when(expenseRepository.findAll()).thenReturn(expenseList);

        StatsDTO stats = statsService.getStats(userId);

        assertEquals(totalIncome, stats.getIncome());
        assertEquals(totalExpense, stats.getExpense());
        assertEquals(totalIncome - totalExpense, stats.getBalance());
        assertEquals(latestIncome, stats.getLatestIncome());
        assertEquals(latestExpense, stats.getLatestExpense());
        assertEquals(500.0, stats.getMaxIncome());
        assertEquals(100.0, stats.getMinIncome());
        assertEquals(200.0, stats.getMaxExpense());
        assertEquals(50.0, stats.getMinExpense());
    }

    @Test
    void testGetStats_WithEmptyIncomeAndExpenseLists() {
        Long userId = 3L;
        when(incomeRepository.sumAllAmountsByUserId(userId)).thenReturn(0.0);
        when(expenseRepository.sumAllAmountsByUserId(userId)).thenReturn(0.0);
        when(incomeRepository.findFirstByUserIdOrderByDateDesc(userId)).thenReturn(Optional.empty());
        when(expenseRepository.findFirstByUserIdOrderByDateDesc(userId)).thenReturn(Optional.empty());
        when(incomeRepository.findAll()).thenReturn(Collections.emptyList());
        when(expenseRepository.findAll()).thenReturn(Collections.emptyList());

        StatsDTO stats = statsService.getStats(userId);

        assertEquals(0.0, stats.getIncome());
        assertEquals(0.0, stats.getExpense());
        assertEquals(0.0, stats.getBalance());
        assertNull(stats.getLatestIncome());
        assertNull(stats.getLatestExpense());
        assertNull(stats.getMaxIncome());
        assertNull(stats.getMinIncome());
        assertNull(stats.getMaxExpense());
        assertNull(stats.getMinExpense());
    }

    private Income createIncome(double amount) {
        Income income = new Income();
        income.setAmount((int) amount);
        return income;
    }

    private Expense createExpense(double amount) {
        Expense expense = new Expense();
        expense.setAmount((int)amount);
        return expense;
    }
}
