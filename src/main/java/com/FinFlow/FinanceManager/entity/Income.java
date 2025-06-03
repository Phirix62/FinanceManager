package com.FinFlow.FinanceManager.entity;

import java.time.LocalDate;

import com.FinFlow.FinanceManager.dto.IncomeDTO;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
public class Income {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private Integer amount;
    private LocalDate date;
    private String category;
    private String description;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public IncomeDTO getIncomeDTO() {
        IncomeDTO incomeDTO = new IncomeDTO();
        incomeDTO.setId(this.id);
        incomeDTO.setTitle(this.title);
        incomeDTO.setAmount(this.amount);
        incomeDTO.setDate(this.date);
        incomeDTO.setCategory(this.category);
        incomeDTO.setDescription(this.description);
        incomeDTO.setUser(this.user);
        return incomeDTO;
    }

}
