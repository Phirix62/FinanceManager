package com.FinFlow.FinanceManager.dto;

import com.FinFlow.FinanceManager.entity.Expense;
import com.FinFlow.FinanceManager.entity.Income;

import lombok.Data;

@Data
public class StatsDTO {

    private Double income;
    private Double expense;

    private Income latestIncome;
    private Expense latestExpense;

    private Double balance;
    private Double minIncome;
    private Double maxIncome;
    private Double minExpense;
    private Double maxExpense;

}
