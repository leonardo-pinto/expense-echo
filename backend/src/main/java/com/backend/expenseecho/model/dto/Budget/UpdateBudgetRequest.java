package com.backend.expenseecho.model.dto.Budget;

import jakarta.validation.constraints.NotBlank;

public class UpdateBudgetRequest {
    @NotBlank(message = "Budget name is required")
    private String name;

    public UpdateBudgetRequest() {};

    public UpdateBudgetRequest(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
