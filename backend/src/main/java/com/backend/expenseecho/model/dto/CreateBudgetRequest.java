package com.backend.expenseecho.model.dto;

import jakarta.validation.constraints.NotBlank;

public class CreateBudgetRequest {

    @NotBlank(message = "Budget name is required")
    private String name;

    public CreateBudgetRequest() {}

    public CreateBudgetRequest(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
