package com.backend.expenseecho.model.dto.Category;

import com.backend.expenseecho.model.entities.Category;

public class CategoryResponse {

    private Integer id;
    private String name;
    private Boolean isDefault;

    public CategoryResponse() {}

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

    public Boolean getDefault() {
        return isDefault;
    }

    public void setDefault(Boolean aDefault) {
        isDefault = aDefault;
    }
}
