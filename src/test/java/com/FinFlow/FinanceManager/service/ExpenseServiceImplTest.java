package com.FinFlow.FinanceManager.service;

import com.FinFlow.FinanceManager.dto.ExpenseDTO;
import com.FinFlow.FinanceManager.entity.Expense;
import com.FinFlow.FinanceManager.repository.ExpenseRepository;
import com.FinFlow.FinanceManager.services.expense.ExpenseServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.time.LocalDate;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;



class ExpenseServiceImplTest {

    @Mock
    private ExpenseRepository expenseRepository;

    @InjectMocks
    private ExpenseServiceImpl expenseService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private ExpenseDTO createExpenseDTO() {
        ExpenseDTO dto = new ExpenseDTO();
        dto.setTitle("Lunch");
        dto.setDescription("Business lunch");
        dto.setCategory("Food");
        dto.setDate(LocalDate.of(2024, 6, 1));
        dto.setAmount(20);
        dto.setUser(null); // Set user if needed
        return dto;
    }

    private Expense createExpense(Long id) {
        Expense expense = new Expense();
        expense.setId(id);
        expense.setTitle("Lunch");
        expense.setDescription("Business lunch");
        expense.setCategory("Food");
        expense.setDate(LocalDate.of(2024, 6, 1));
        expense.setAmount(20);
        expense.setUser(null); // Set user if needed
        return expense;
    }

    @Test
    void testPostExpense() {
        ExpenseDTO dto = createExpenseDTO();
        Expense savedExpense = createExpense(1L);

        when(expenseRepository.save(any(Expense.class))).thenReturn(savedExpense);

        Expense result = expenseService.postExpense(dto);

        assertNotNull(result);
        assertEquals("Lunch", result.getTitle());
        verify(expenseRepository, times(1)).save(any(Expense.class));
    }

    @Test
    void testUpdateExpense_Success() {
        ExpenseDTO dto = createExpenseDTO();
        Expense existingExpense = createExpense(1L);

        when(expenseRepository.findById(1L)).thenReturn(Optional.of(existingExpense));
        when(expenseRepository.save(any(Expense.class))).thenReturn(existingExpense);

        Expense result = expenseService.updateExpense(1L, dto);

        assertNotNull(result);
        assertEquals("Lunch", result.getTitle());
        verify(expenseRepository).findById(1L);
        verify(expenseRepository).save(existingExpense);
    }

    @Test
    void testUpdateExpense_NotFound() {
        ExpenseDTO dto = createExpenseDTO();
        when(expenseRepository.findById(2L)).thenReturn(Optional.empty());

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () ->
                expenseService.updateExpense(2L, dto));
        assertTrue(ex.getMessage().contains("Expense not found with id: 2"));
    }

    @Test
    void testGetAllExpenses() {
        Expense e1 = createExpense(1L);
        e1.setDate(LocalDate.of(2024, 6, 2));
        Expense e2 = createExpense(2L);
        e2.setDate(LocalDate.of(2024, 6, 1));
        List<Expense> expenses = Arrays.asList(e1, e2);

        when(expenseRepository.findAll()).thenReturn(expenses);

        List<Expense> result = expenseService.getAllExpenses();

        assertEquals(2, result.size());
        assertEquals(e1, result.get(0)); // Sorted by date descending
        assertEquals(e2, result.get(1));
    }

    @Test
    void testGetExpenseById_Success() {
        Expense expense = createExpense(1L);
        when(expenseRepository.findById(1L)).thenReturn(Optional.of(expense));

        Expense result = expenseService.getExpenseById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void testGetExpenseById_NotFound() {
        when(expenseRepository.findById(2L)).thenReturn(Optional.empty());

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () ->
                expenseService.getExpenseById(2L));
        assertTrue(ex.getMessage().contains("Expense not found with id: 2"));
    }

    @Test
    void testGetExpensesByUserId() {
        Expense e1 = createExpense(1L);
        e1.setDate(LocalDate.of(2024, 6, 2));
        Expense e2 = createExpense(2L);
        e2.setDate(LocalDate.of(2024, 6, 1));
        List<Expense> expenses = Arrays.asList(e1, e2);

        when(expenseRepository.findByUserId(10L)).thenReturn(expenses);

        List<Expense> result = expenseService.getExpensesByUserId(10L);

        assertEquals(2, result.size());
        assertEquals(e1, result.get(0)); // Sorted by date descending
        assertEquals(e2, result.get(1));
    }

    @Test
    void testDeleteExpense_Success() {
        Expense expense = createExpense(1L);
        when(expenseRepository.findById(1L)).thenReturn(Optional.of(expense));
        doNothing().when(expenseRepository).deleteById(1L);

        assertDoesNotThrow(() -> expenseService.deleteExpense(1L));
        verify(expenseRepository).deleteById(1L);
    }

    @Test
    void testDeleteExpense_NotFound() {
        when(expenseRepository.findById(2L)).thenReturn(Optional.empty());

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () ->
                expenseService.deleteExpense(2L));
        assertTrue(ex.getMessage().contains("Expense not found with id: 2"));
    }
}
