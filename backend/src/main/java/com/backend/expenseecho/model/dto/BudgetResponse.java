package com.backend.expenseecho.model.dto;

import com.backend.expenseecho.model.entities.Budget;

import java.util.Date;

public class BudgetResponse {
    private Integer id;
    private String name;
    private Date createdAt;

    public BudgetResponse(Budget budget) {
        this.id = budget.getId();
        this.name = budget.getName();
        this.createdAt = budget.getCreatedAt();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
