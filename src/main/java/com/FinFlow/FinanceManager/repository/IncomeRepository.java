package com.FinFlow.FinanceManager.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.FinFlow.FinanceManager.entity.Income;
import java.time.LocalDate;


@Repository
public interface IncomeRepository extends JpaRepository<Income, Long>{

    List<Income> findByUserIdAndDateBetween(Long userId, LocalDate startDate, LocalDate endDate);

    @Query("SELECT SUM(i.amount) FROM Income i WHERE i.user.id = :userId")
    Double sumAllAmountsByUserId(Long userId);

    Optional<Income> findFirstByUserIdOrderByDateDesc(Long userId);

    List<Income> findByUserId(Long userId);

}
