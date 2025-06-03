package com.FinFlow.FinanceManager.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.FinFlow.FinanceManager.dto.IncomeDTO;
import com.FinFlow.FinanceManager.entity.Income;
import com.FinFlow.FinanceManager.services.income.IncomeService;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Controller for managing income-related operations.
 * Provides endpoints for creating, retrieving, updating, and deleting incomes.
 */
@RestController
@RequestMapping("/api/income")
@RequiredArgsConstructor
@CrossOrigin("*")
public class IncomeController {

    private static final Logger logger = LoggerFactory.getLogger(IncomeController.class);

    private final IncomeService incomeService;

    /**
     * Creates a new income entry.
     *
     * @param incomeDTO the income data transfer object
     * @return ResponseEntity with created income or error status
     */
    @PostMapping
    public ResponseEntity<?> postIncome(@RequestBody IncomeDTO incomeDTO) {
        logger.info("Received request to create income: {}", incomeDTO);
        Income createdIncome = incomeService.postIncome(incomeDTO);
        if (createdIncome != null) {
            logger.info("Income created successfully: {}", createdIncome);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdIncome);
        } else {
            logger.warn("Failed to create income: {}", incomeDTO);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    /**
     * Retrieves all incomes.
     *
     * @return ResponseEntity with list of all incomes
     */
    @GetMapping("/all")
    public ResponseEntity<?> getAllIncomes() {
        logger.info("Received request to retrieve all incomes");
        return ResponseEntity.ok(incomeService.getAllIncomes());
    }

    /**
     * Updates an existing income by ID.
     *
     * @param id the ID of the income to update
     * @param incomeDTO the updated income data
     * @return ResponseEntity with updated income or error status
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateIncome(@PathVariable Long id, @RequestBody IncomeDTO incomeDTO) {
        logger.info("Received request to update income with id: {}", id);
        try {
            return ResponseEntity.ok(incomeService.updateIncome(id, incomeDTO));
        } catch (EntityNotFoundException ex) {
            logger.warn("Income not found for update: {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (Exception e) {
            logger.error("Error updating income with id {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while updating the income.");
        }
    }

    /**
     * Retrieves an income by its ID.
     *
     * @param id the ID of the income
     * @return ResponseEntity with the income or error status
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getIncomeById(@PathVariable Long id) {
        logger.info("Received request to retrieve income with id: {}", id);
        try {
            return ResponseEntity.ok(incomeService.getIncomeById(id));
        } catch (EntityNotFoundException ex) {
            logger.warn("Income not found: {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (Exception e) {
            logger.error("Error retrieving income with id {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while retrieving the income.");
        }
    }

    /**
     * Retrieves all incomes for a specific user.
     *
     * @param userId the ID of the user
     * @return ResponseEntity with list of incomes for the user
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getIncomesByUserId(@PathVariable Long userId) {
        logger.info("Received request to retrieve incomes for user id: {}", userId);
        return ResponseEntity.ok(incomeService.getIncomesByUserId(userId));
    }

    /**
     * Deletes an income by its ID.
     *
     * @param id the ID of the income to delete
     * @return ResponseEntity with status of the operation
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteIncome(@PathVariable Long id) {
        logger.info("Received request to delete income with id: {}", id);
        try {
            incomeService.deleteIncome(id);
            logger.info("Income deleted successfully: {}", id);
            return ResponseEntity.ok(null);
        } catch (EntityNotFoundException ex) {
            logger.warn("Income not found for deletion: {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (Exception e) {
            logger.error("Error deleting income with id {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while deleting the income.");
        }
    }

}
