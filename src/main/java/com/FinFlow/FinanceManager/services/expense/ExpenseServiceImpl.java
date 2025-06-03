package com.FinFlow.FinanceManager.services.expense;

import com.FinFlow.FinanceManager.dto.ExpenseDTO;
import com.FinFlow.FinanceManager.entity.Expense;
import com.FinFlow.FinanceManager.repository.ExpenseRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Service implementation for managing expenses.
 */
@Service
@RequiredArgsConstructor
public class ExpenseServiceImpl implements ExpenseService {

    private static final Logger logger = LoggerFactory.getLogger(ExpenseServiceImpl.class);

    private final ExpenseRepository expenseRepository;

    /**
     * Creates a new expense based on the provided ExpenseDTO.
     *
     * @param expenseDTO the expense data transfer object
     * @return the saved Expense entity
     */
    public Expense postExpense(ExpenseDTO expenseDTO) {
        logger.info("Creating new expense with title: {}", expenseDTO.getTitle());
        Expense expense = new Expense();
        return saveOrUpdateExpense(expense, expenseDTO);
    }

    /**
     * Saves or updates an expense entity with data from the provided ExpenseDTO.
     *
     * @param expense the expense entity to update
     * @param expenseDTO the expense data transfer object
     * @return the saved or updated Expense entity
     */
    private Expense saveOrUpdateExpense(Expense expense, ExpenseDTO expenseDTO){
        expense.setTitle(expenseDTO.getTitle());
        expense.setDescription(expenseDTO.getDescription());
        expense.setCategory(expenseDTO.getCategory());
        expense.setDate(expenseDTO.getDate());
        expense.setAmount(expenseDTO.getAmount());
        expense.setUser(expenseDTO.getUser());

        logger.debug("Saving expense: {}", expense);
        return expenseRepository.save(expense);
    }

    /**
     * Updates an existing expense with the given id using data from the provided ExpenseDTO.
     *
     * @param id the id of the expense to update
     * @param expenseDTO the expense data transfer object
     * @return the updated Expense entity
     * @throws EntityNotFoundException if the expense is not found
     */
    public Expense updateExpense(Long id, ExpenseDTO expenseDTO) {
        logger.info("Updating expense with id: {}", id);
        Optional<Expense> optionalExpense = expenseRepository.findById(id);
        if (optionalExpense.isPresent()) {
            return saveOrUpdateExpense(optionalExpense.get(), expenseDTO);
        } else {
            logger.warn("Expense not found with id: {}", id);
            throw new EntityNotFoundException("Expense not found with id: " + id);
        }
    }

    /**
     * Retrieves all expenses, sorted by date in descending order.
     *
     * @return a list of all Expense entities
     */
    public List<Expense> getAllExpenses() {
        logger.info("Retrieving all expenses");
        return expenseRepository.findAll().stream()
                .sorted(Comparator.comparing(Expense::getDate).reversed())
                .collect(Collectors.toList());
    }

    /**
     * Retrieves an expense by its id.
     *
     * @param id the id of the expense
     * @return the Expense entity
     * @throws EntityNotFoundException if the expense is not found
     */
    public Expense getExpenseById(Long id) {
        logger.info("Retrieving expense with id: {}", id);
        Optional<Expense> optionalExpense = expenseRepository.findById(id);
        if (optionalExpense.isPresent()) {
            return optionalExpense.get();
        } else {
            logger.warn("Expense not found with id: {}", id);
            throw new EntityNotFoundException("Expense not found with id: " + id);
        }
    }

    /**
     * Retrieves all expenses for a specific user, sorted by date in descending order.
     *
     * @param userId the id of the user
     * @return a list of Expense entities for the user
     */
    public List<Expense> getExpensesByUserId(Long userId) {
        logger.info("Retrieving expenses for user with id: {}", userId);
        return expenseRepository.findByUserId(userId).stream()
                .sorted(Comparator.comparing(Expense::getDate).reversed())
                .collect(Collectors.toList());
    }

    /**
     * Deletes an expense by its id.
     *
     * @param id the id of the expense to delete
     * @throws EntityNotFoundException if the expense is not found
     */
    public void deleteExpense(Long id) {
        logger.info("Deleting expense with id: {}", id);
        Optional<Expense> optionalExpense = expenseRepository.findById(id);
        if (optionalExpense.isPresent()) {
            expenseRepository.deleteById(id);
            logger.debug("Expense deleted with id: {}", id);
        } else {
            logger.warn("Expense not found with id: {}", id);
            throw new EntityNotFoundException("Expense not found with id: " + id);
        }
    }

}
