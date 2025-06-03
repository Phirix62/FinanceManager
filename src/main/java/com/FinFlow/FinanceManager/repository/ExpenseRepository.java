package com.FinFlow.FinanceManager.repository;

import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.FinFlow.FinanceManager.entity.Expense;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {

    List<Expense> findByUserIdAndDateBetween(Long userId, LocalDate startDate, LocalDate endDate);

    @Query("SELECT SUM(e.amount) FROM Expense e WHERE e.user.id = :userId")
    Double sumAllAmountsByUserId(Long userId);

    Optional<Expense> findFirstByUserIdOrderByDateDesc(Long userId);

    List<Expense> findByUserId(Long userId);



}
