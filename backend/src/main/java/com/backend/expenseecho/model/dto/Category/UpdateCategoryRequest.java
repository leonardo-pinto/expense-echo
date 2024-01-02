package com.backend.expenseecho.model.dto.Category;

import jakarta.validation.constraints.NotBlank;

public class UpdateCategoryRequest {
    @NotBlank(message = "Category name is required")
    private String name;

    public UpdateCategoryRequest() {}

    public UpdateCategoryRequest(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
