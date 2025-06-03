package com.FinFlow.FinanceManager.services.income;

import java.util.List;

import com.FinFlow.FinanceManager.dto.IncomeDTO;
import com.FinFlow.FinanceManager.entity.Income;

public interface IncomeService {

    Income postIncome(IncomeDTO incomeDTO);

    List<IncomeDTO> getAllIncomes();

    List<IncomeDTO> getIncomesByUserId(Long userId);

    Income updateIncome(Long id, IncomeDTO incomeDTO);

    IncomeDTO getIncomeById(Long id);

    void deleteIncome(Long id);

}
