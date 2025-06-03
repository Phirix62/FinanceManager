package com.FinFlow.FinanceManager.dto;

import java.time.LocalDate;
import com.FinFlow.FinanceManager.entity.User;


import lombok.Data;

@Data
public class ExpenseDTO {

    private Long id;
    private String title;
    private String description;
    private String category;
    private LocalDate date;
    private Integer amount;
    private User user;

}
