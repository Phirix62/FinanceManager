package com.FinFlow.FinanceManager.dto;

import java.util.List;

import com.FinFlow.FinanceManager.entity.Expense;
import com.FinFlow.FinanceManager.entity.Income;

import lombok.Data;

@Data
public class GraphDTO {

    private List<Expense> expenseList;

    private List<Income> incomeList;

}
