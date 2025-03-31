package com.FinFlow.FinanceManager.services.expense;

import java.util.List;

import com.FinFlow.FinanceManager.dto.ExpenseDTO;
import com.FinFlow.FinanceManager.entity.Expense;

public interface ExpenseService {

    Expense postExpense(ExpenseDTO expenseDTO);

    List<Expense> getAllExpenses();

    Expense getExpenseById(Long id);

    Expense updateExpense(Long id, ExpenseDTO expenseDTO);

    void deleteExpense(Long id);

}
