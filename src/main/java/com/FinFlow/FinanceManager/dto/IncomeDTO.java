package com.FinFlow.FinanceManager.dto;

import java.time.LocalDate;
import com.FinFlow.FinanceManager.entity.User;

import lombok.Data;

@Data
public class IncomeDTO {

    private Long id;
    private String title;
    private Integer amount;
    private LocalDate date;
    private String category;
    private String description;
    private User user;

}
