package com.FinFlow.FinanceManager.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.FinFlow.FinanceManager.dto.ExpenseDTO;
import com.FinFlow.FinanceManager.entity.Expense;
import com.FinFlow.FinanceManager.services.expense.ExpenseService;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Controller for managing expense-related endpoints.
 */
@RestController
@RequestMapping("/api/expense")
@RequiredArgsConstructor
@CrossOrigin("*")
public class ExpenseController {

    private static final Logger logger = LoggerFactory.getLogger(ExpenseController.class);

    private final ExpenseService expenseService;

    /**
     * Creates a new expense.
     *
     * @param dto the expense data transfer object
     * @return ResponseEntity with created expense or error status
     */
    @PostMapping
    public ResponseEntity<?> postExpense(@RequestBody ExpenseDTO dto){
        logger.info("Received request to create expense: {}", dto);
        Expense createdExpense = expenseService.postExpense(dto);
        if(createdExpense != null) {
            logger.info("Expense created successfully: {}", createdExpense);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdExpense);
        } else {
            logger.warn("Failed to create expense: {}", dto);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    /**
     * Retrieves all expenses.
     *
     * @return ResponseEntity with list of all expenses
     */
    @GetMapping("/all")
    public ResponseEntity<?> getAllExpenses(){
        logger.info("Received request to get all expenses");
        return ResponseEntity.ok(expenseService.getAllExpenses());
    }

    /**
     * Retrieves an expense by its ID.
     *
     * @param id the expense ID
     * @return ResponseEntity with the expense or error status
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getExpenseById(@PathVariable Long id){
        logger.info("Received request to get expense by id: {}", id);
        try{
            return ResponseEntity.ok(expenseService.getExpenseById(id));
        } catch (EntityNotFoundException ex) {
            logger.warn("Expense not found with id: {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (Exception e){
            logger.error("Error retrieving expense with id {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while retrieving the expense.");
        }
    }

    /**
     * Retrieves expenses by user ID.
     *
     * @param userId the user ID
     * @return ResponseEntity with list of expenses for the user
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getExpensesByUserId(@PathVariable Long userId){
        logger.info("Received request to get expenses for user id: {}", userId);
        return ResponseEntity.ok(expenseService.getExpensesByUserId(userId));
    }

    /**
     * Updates an existing expense.
     *
     * @param id the expense ID
     * @param dto the updated expense data
     * @return ResponseEntity with updated expense or error status
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateExpense(@PathVariable Long id, @RequestBody ExpenseDTO dto){
        logger.info("Received request to update expense id: {} with data: {}", id, dto);
        try{
            return ResponseEntity.ok(expenseService.updateExpense(id, dto));
        } catch (EntityNotFoundException ex){
            logger.warn("Expense not found for update with id: {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (Exception e){
            logger.error("Error updating expense with id {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while updating the expense.");
        }
    }

    /**
     * Deletes an expense by its ID.
     *
     * @param id the expense ID
     * @return ResponseEntity with status of the operation
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteExpense (@PathVariable Long id){
        logger.info("Received request to delete expense id: {}", id);
        try{
            expenseService.deleteExpense(id);
            logger.info("Expense deleted successfully: {}", id);
            return ResponseEntity.ok(null);
        } catch (EntityNotFoundException ex){
            logger.warn("Expense not found for deletion with id: {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (Exception e){
            logger.error("Error deleting expense with id {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while deleting the expense.");
        }
    }
}
