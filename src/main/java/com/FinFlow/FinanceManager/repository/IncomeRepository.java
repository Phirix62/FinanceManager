package com.FinFlow.FinanceManager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.FinFlow.FinanceManager.entity.Income;

@Repository
public interface IncomeRepository extends JpaRepository<Income, Long>{

}
