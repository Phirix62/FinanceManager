package com.FinFlow.FinanceManager.controller;

import com.FinFlow.FinanceManager.dto.ExpenseDTO;
import com.FinFlow.FinanceManager.entity.Expense;
import com.FinFlow.FinanceManager.services.expense.ExpenseService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;




public class ExpenseControllerTest {

    @Mock
    private ExpenseService expenseService;

    @InjectMocks
    private ExpenseController expenseController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void postExpense_shouldReturnCreated_whenExpenseIsCreated() {
        ExpenseDTO dto = new ExpenseDTO();
        Expense expense = new Expense();
        when(expenseService.postExpense(dto)).thenReturn(expense);

        ResponseEntity<?> response = expenseController.postExpense(dto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(expense, response.getBody());
    }

    @Test
    void postExpense_shouldReturnBadRequest_whenExpenseIsNull() {
        ExpenseDTO dto = new ExpenseDTO();
        when(expenseService.postExpense(dto)).thenReturn(null);

        ResponseEntity<?> response = expenseController.postExpense(dto);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void getAllExpenses_shouldReturnListOfExpenses() {
        List<Expense> expenses = Arrays.asList(new Expense(), new Expense());
        when(expenseService.getAllExpenses()).thenReturn(expenses);

        ResponseEntity<?> response = expenseController.getAllExpenses();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expenses, response.getBody());
    }

    @Test
    void getExpenseById_shouldReturnExpense_whenFound() {
        Expense expense = new Expense();
        when(expenseService.getExpenseById(1L)).thenReturn(expense);

        ResponseEntity<?> response = expenseController.getExpenseById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expense, response.getBody());
    }

    @Test
    void getExpenseById_shouldReturnNotFound_whenEntityNotFound() {
        when(expenseService.getExpenseById(1L)).thenThrow(new EntityNotFoundException("Not found"));

        ResponseEntity<?> response = expenseController.getExpenseById(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Not found", response.getBody());
    }

    @Test
    void getExpenseById_shouldReturnInternalServerError_onOtherException() {
        when(expenseService.getExpenseById(1L)).thenThrow(new RuntimeException());

        ResponseEntity<?> response = expenseController.getExpenseById(1L);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("An error occurred while retrieving the expense.", response.getBody());
    }

    @Test
    void getExpensesByUserId_shouldReturnExpenses() {
        List<Expense> expenses = Collections.singletonList(new Expense());
        when(expenseService.getExpensesByUserId(2L)).thenReturn(expenses);

        ResponseEntity<?> response = expenseController.getExpensesByUserId(2L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expenses, response.getBody());
    }

    @Test
    void updateExpense_shouldReturnUpdatedExpense_whenFound() {
        ExpenseDTO dto = new ExpenseDTO();
        Expense updatedExpense = new Expense();
        when(expenseService.updateExpense(1L, dto)).thenReturn(updatedExpense);

        ResponseEntity<?> response = expenseController.updateExpense(1L, dto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedExpense, response.getBody());
    }

    @Test
    void updateExpense_shouldReturnNotFound_whenEntityNotFound() {
        ExpenseDTO dto = new ExpenseDTO();
        when(expenseService.updateExpense(1L, dto)).thenThrow(new EntityNotFoundException("Not found"));

        ResponseEntity<?> response = expenseController.updateExpense(1L, dto);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Not found", response.getBody());
    }

    @Test
    void updateExpense_shouldReturnInternalServerError_onOtherException() {
        ExpenseDTO dto = new ExpenseDTO();
        when(expenseService.updateExpense(1L, dto)).thenThrow(new RuntimeException());

        ResponseEntity<?> response = expenseController.updateExpense(1L, dto);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("An error occurred while updating the expense.", response.getBody());
    }

    @Test
    void deleteExpense_shouldReturnOk_whenDeleted() {
        doNothing().when(expenseService).deleteExpense(1L);

        ResponseEntity<?> response = expenseController.deleteExpense(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void deleteExpense_shouldReturnNotFound_whenEntityNotFound() {
        doThrow(new EntityNotFoundException("Not found")).when(expenseService).deleteExpense(1L);

        ResponseEntity<?> response = expenseController.deleteExpense(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Not found", response.getBody());
    }

    @Test
    void deleteExpense_shouldReturnInternalServerError_onOtherException() {
        doThrow(new RuntimeException()).when(expenseService).deleteExpense(1L);

        ResponseEntity<?> response = expenseController.deleteExpense(1L);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("An error occurred while deleting the expense.", response.getBody());
    }
}
